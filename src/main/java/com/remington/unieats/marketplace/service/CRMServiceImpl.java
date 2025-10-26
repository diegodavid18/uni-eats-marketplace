package com.remington.unieats.marketplace.service;

import com.remington.unieats.marketplace.model.entity.*;
import com.remington.unieats.marketplace.model.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

@Service
public class CRMServiceImpl implements CRMService {

    private static final Logger logger = Logger.getLogger(CRMServiceImpl.class.getName());

    @Autowired private CustomerProfileRepository profileRepository;
    @Autowired private BehaviorLogRepository behaviorLogRepository;
    @Autowired private MarketingCampaignRepository campaignRepository;
    @Autowired private CampaignSendRepository sendRepository;
    @Autowired private EmailTemplateRepository templateRepository;
    @Autowired private CouponRepository couponRepository;
    @Autowired private UserCouponUsageRepository usageRepository;
    @Autowired private LoyaltyPointsRepository loyaltyRepository;
    @Autowired private LoyaltyTransactionRepository transactionRepository;
    @Autowired private UserEngagementTrackingRepository engagementRepository;
    @Autowired private CampaignLinkRepository linkRepository;
    @Autowired private EmailService emailService;

    @Override
    public CustomerProfile createOrUpdateProfile(Usuario usuario) {
        Optional<CustomerProfile> existing = profileRepository.findByUsuario(usuario);
        if (existing.isPresent()) return existing.get();
        
        CustomerProfile profile = new CustomerProfile(usuario);
        profile.setSegment("NEW");
        return profileRepository.save(profile);
    }

    @Override
    public Optional<CustomerProfile> getProfileByUsuario(Usuario usuario) {
        return profileRepository.findByUsuario(usuario);
    }

    @Override
    public void updateSegment(Long profileId, String segment) {
        profileRepository.findById(profileId).ifPresent(p -> {
            p.setSegment(segment);
            p.setUpdatedAt(LocalDateTime.now());
            profileRepository.save(p);
        });
    }

    @Override
    public List<CustomerProfile> getProfilesBySegment(String segment) {
        return profileRepository.findBySegment(segment);
    }

    @Override
    public void logBehavior(Usuario usuario, String tipoEvento, String entidadTipo, Long entidadId, String detalles) {
        BehaviorLog log = new BehaviorLog(usuario, tipoEvento);
        log.setEntidadTipo(entidadTipo);
        log.setEntidadId(entidadId);
        log.setDetallesJson(detalles);
        behaviorLogRepository.save(log);
    }

    @Override
    public List<BehaviorLog> getUserBehaviors(Usuario usuario) {
        return behaviorLogRepository.findByUsuarioOrderByCreatedAtDesc(usuario);
    }

    @Override
    public List<BehaviorLog> getBehaviorsByType(Usuario usuario, String tipoEvento) {
        return behaviorLogRepository.findByUsuarioAndTipoEvento(usuario, tipoEvento);
    }

    @Override
    public MarketingCampaign createCampaign(String nombre, String tipo, String descripcion) {
        MarketingCampaign campaign = new MarketingCampaign();
        campaign.setNombre(nombre);
        campaign.setTipo(tipo);
        campaign.setDescripcion(descripcion);
        campaign.setEstado("DRAFT");
        return campaignRepository.save(campaign);
    }

    @Override
    public List<MarketingCampaign> getCampaignsByStatus(String estado) {
        return campaignRepository.findByEstado(estado);
    }

    @Override
    public void updateCampaignStatus(Long campaignId, String newStatus) {
        campaignRepository.findById(campaignId).ifPresent(c -> {
            c.setEstado(newStatus);
            c.setUpdatedAt(LocalDateTime.now());
            campaignRepository.save(c);
        });
    }

    @Override
    public CampaignSend sendCampaignEmail(Long campaignId, Usuario usuario, EmailTemplate template) {
        Optional<MarketingCampaign> campaign = campaignRepository.findById(campaignId);
        if (campaign.isEmpty()) return null;

        CampaignSend send = new CampaignSend();
        send.setCampaign(campaign.get());
        send.setUsuario(usuario);
        send.setEmailTemplate(template);
        send.setEmailAddress(usuario.getCorreo());
        send.setAsunto(template.getAsuntoTemplate());
        send.setContenidoEnviado(template.getContenidoHtml());
        send.setFechaEnvio(LocalDateTime.now());
        
        // Intentar enviar el correo real
        boolean enviado = emailService.enviarEmailHtml(
            template.getAsuntoTemplate(),
            template.getContenidoHtml(),
            usuario.getCorreo()
        );
        
        // Establecer estado según el resultado
        send.setEstado(enviado ? "SENT" : "FAILED");
        
        CampaignSend saved = sendRepository.save(send);
        
        if (enviado) {
            logger.info("✓ Correo enviado a " + usuario.getCorreo() + " - Campaña ID: " + campaignId);
        } else {
            logger.warning("✗ Fallo al enviar correo a " + usuario.getCorreo());
        }
        
        return saved;
    }

    @Override
    public List<CampaignSend> getCampaignSendStats(Long campaignId) {
        Optional<MarketingCampaign> campaign = campaignRepository.findById(campaignId);
        return campaign.map(sendRepository::findByCampaign).orElse(List.of());
    }

    @Override
    public void markEmailAsOpened(Long sendId) {
        sendRepository.findById(sendId).ifPresent(s -> {
            s.setEstado("OPENED");
            s.setFechaApertura(LocalDateTime.now());
            sendRepository.save(s);
        });
    }

    @Override
    public void trackLinkClick(String trackingLink) {
        linkRepository.findByEnlaceTrackeable(trackingLink).ifPresent(l -> {
            l.setClicsTotal(l.getClicsTotal() + 1);
            linkRepository.save(l);
        });
    }

    @Override
    public EmailTemplate createEmailTemplate(String nombre, String asunto, String contenidoHtml, String categoria) {
        EmailTemplate template = new EmailTemplate(nombre, contenidoHtml);
        template.setAsuntoTemplate(asunto);
        template.setCategoria(categoria);
        return templateRepository.save(template);
    }

    @Override
    public Optional<EmailTemplate> getTemplateByName(String nombre) {
        return templateRepository.findByNombre(nombre);
    }

    @Override
    public List<EmailTemplate> getTemplatesByCategory(String categoria) {
        return templateRepository.findByCategoria(categoria);
    }

    @Override
    public Coupon createCoupon(String codigo, String tipoDescuento, Double valor) {
        Coupon coupon = new Coupon();
        coupon.setCodigo(codigo);
        coupon.setTipoDescuento(tipoDescuento);
        coupon.setValorDescuento(BigDecimal.valueOf(valor));
        coupon.setActivo(true);
        return couponRepository.save(coupon);
    }

    @Override
    public Optional<Coupon> validateCoupon(String codigo) {
        Optional<Coupon> coupon = couponRepository.findByCodigo(codigo);
        if (coupon.isEmpty() || !coupon.get().getActivo()) return Optional.empty();
        if (coupon.get().getFechaExpiracion() != null && 
            coupon.get().getFechaExpiracion().isBefore(LocalDateTime.now())) {
            return Optional.empty();
        }
        if (coupon.get().getCantidadMaxima() != null && 
            coupon.get().getCantidadUsada() >= coupon.get().getCantidadMaxima()) {
            return Optional.empty();
        }
        return coupon;
    }

    @Override
    public void applyCoupon(Usuario usuario, String codigo, Long pedidoId) {
        validateCoupon(codigo).ifPresent(coupon -> {
            UserCouponUsage usage = new UserCouponUsage();
            usage.setUsuario(usuario);
            usage.setCoupon(coupon);
            usage.setPedidoId(pedidoId);
            usage.setMontoDescuento(coupon.getValorDescuento());
            usageRepository.save(usage);
            
            coupon.setCantidadUsada(coupon.getCantidadUsada() + 1);
            couponRepository.save(coupon);
        });
    }

    @Override
    public List<Coupon> getActiveCoupons() {
        return couponRepository.findByActivo(true);
    }

    @Override
    public void addLoyaltyPoints(Usuario usuario, Integer puntos, String razon) {
        Optional<LoyaltyPoints> loyalty = loyaltyRepository.findByUsuario(usuario);
        LoyaltyPoints loyaltyPoints;
        
        if (loyalty.isEmpty()) {
            loyaltyPoints = new LoyaltyPoints(usuario);
        } else {
            loyaltyPoints = loyalty.get();
        }
        
        loyaltyPoints.setPuntosTotales(loyaltyPoints.getPuntosTotales() + puntos);
        loyaltyPoints.setPuntosDisponibles(loyaltyPoints.getPuntosDisponibles() + puntos);
        loyaltyPoints.setUpdatedAt(LocalDateTime.now());
        loyaltyRepository.save(loyaltyPoints);
        
        // Log transaction
        LoyaltyTransaction transaction = new LoyaltyTransaction();
        transaction.setLoyaltyPoints(loyaltyPoints);
        transaction.setTipoTransaccion("EARNED");
        transaction.setCantidad(puntos);
        transaction.setDescripcion(razon);
        transactionRepository.save(transaction);
    }

    @Override
    public void redeemLoyaltyPoints(Usuario usuario, Integer puntos) {
        loyaltyRepository.findByUsuario(usuario).ifPresent(loyalty -> {
            if (loyalty.getPuntosDisponibles() >= puntos) {
                loyalty.setPuntosDisponibles(loyalty.getPuntosDisponibles() - puntos);
                loyalty.setPuntosCanjeados(loyalty.getPuntosCanjeados() + puntos);
                loyalty.setUpdatedAt(LocalDateTime.now());
                loyaltyRepository.save(loyalty);
                
                LoyaltyTransaction transaction = new LoyaltyTransaction();
                transaction.setLoyaltyPoints(loyalty);
                transaction.setTipoTransaccion("REDEEMED");
                transaction.setCantidad(puntos);
                transactionRepository.save(transaction);
            }
        });
    }

    @Override
    public Optional<LoyaltyPoints> getLoyaltyPoints(Usuario usuario) {
        return loyaltyRepository.findByUsuario(usuario);
    }

    @Override
    public void updateEngagementScore(Usuario usuario) {
        Optional<UserEngagementTracking> tracking = engagementRepository.findByUsuario(usuario);
        UserEngagementTracking engagement;
        
        if (tracking.isEmpty()) {
            engagement = new UserEngagementTracking(usuario);
        } else {
            engagement = tracking.get();
        }
        
        BigDecimal score = calculateEngagementScore(engagement);
        engagement.setEngagementScore(score);
        engagement.setUpdatedAt(LocalDateTime.now());
        engagementRepository.save(engagement);
    }

    @Override
    public Optional<UserEngagementTracking> getEngagementTracking(Usuario usuario) {
        return engagementRepository.findByUsuario(usuario);
    }

    /**
     * Envía una campaña masiva a todos los usuarios de un segmento
     */
    public int enviarCampanaPorSegmento(Long campaignId, String segmento, Long templateId) {
        Optional<MarketingCampaign> campaign = campaignRepository.findById(campaignId);
        Optional<EmailTemplate> template = templateRepository.findById(templateId);
        
        if (campaign.isEmpty() || template.isEmpty()) {
            logger.warning("Campaña o plantilla no encontrada");
            return 0;
        }

        List<CustomerProfile> profiles = profileRepository.findBySegment(segmento);
        int enviados = 0;

        for (CustomerProfile profile : profiles) {
            Usuario usuario = profile.getUsuario();
            CampaignSend send = sendCampaignEmail(campaignId, usuario, template.get());
            if (send != null && "SENT".equals(send.getEstado())) {
                enviados++;
            }
        }

        campaign.get().setEstado("ACTIVE");
        campaignRepository.save(campaign.get());
        logger.info("Campaña " + campaignId + " enviada a " + enviados + " usuarios del segmento: " + segmento);
        
        return enviados;
    }

    /**
     * Obtiene estadísticas de una campaña
     */
    public java.util.Map<String, Object> obtenerEstadisticasCampana(Long campaignId) {
        java.util.Map<String, Object> stats = new java.util.HashMap<>();
        
        Optional<MarketingCampaign> campaign = campaignRepository.findById(campaignId);
        if (campaign.isEmpty()) return stats;

        List<CampaignSend> sends = sendRepository.findByCampaign(campaign.get());
        
        long totalEnviados = sends.size();
        long abiertos = sends.stream().filter(s -> "OPENED".equals(s.getEstado())).count();
        long fallidos = sends.stream().filter(s -> "FAILED".equals(s.getEstado())).count();
        
        stats.put("totalEnviados", totalEnviados);
        stats.put("abiertos", abiertos);
        stats.put("fallidos", fallidos);
        stats.put("tazaApertura", totalEnviados > 0 ? ((double) abiertos / totalEnviados * 100) : 0);
        stats.put("estado", campaign.get().getEstado());
        
        return stats;
    }

    private BigDecimal calculateEngagementScore(UserEngagementTracking tracking) {
        BigDecimal score = BigDecimal.ZERO;
        
        if (tracking.getEmailAbiertoesMes() != null) 
            score = score.add(BigDecimal.valueOf(tracking.getEmailAbiertoesMes() * 2));
        if (tracking.getEmailClicsMes() != null) 
            score = score.add(BigDecimal.valueOf(tracking.getEmailClicsMes() * 3));
        if (tracking.getReviewsEscritos() != null) 
            score = score.add(BigDecimal.valueOf(tracking.getReviewsEscritos() * 5));
        if (tracking.getReferidosExitosos() != null) 
            score = score.add(BigDecimal.valueOf(tracking.getReferidosExitosos() * 10));
        
        return score.min(BigDecimal.valueOf(100));
    }
}
