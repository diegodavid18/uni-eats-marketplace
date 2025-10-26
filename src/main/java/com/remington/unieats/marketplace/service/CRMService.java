package com.remington.unieats.marketplace.service;

import com.remington.unieats.marketplace.model.entity.*;
import java.util.List;
import java.util.Optional;

public interface CRMService {
    // Customer Profiles
    CustomerProfile createOrUpdateProfile(Usuario usuario);
    Optional<CustomerProfile> getProfileByUsuario(Usuario usuario);
    void updateSegment(Long profileId, String segment);
    List<CustomerProfile> getProfilesBySegment(String segment);
    
    // Behavior Logs
    void logBehavior(Usuario usuario, String tipoEvento, String entidadTipo, Long entidadId, String detalles);
    List<BehaviorLog> getUserBehaviors(Usuario usuario);
    List<BehaviorLog> getBehaviorsByType(Usuario usuario, String tipoEvento);
    
    // Campaigns
    MarketingCampaign createCampaign(String nombre, String tipo, String descripcion);
    List<MarketingCampaign> getCampaignsByStatus(String estado);
    void updateCampaignStatus(Long campaignId, String newStatus);
    
    // Campaign Sends
    CampaignSend sendCampaignEmail(Long campaignId, Usuario usuario, EmailTemplate template);
    List<CampaignSend> getCampaignSendStats(Long campaignId);
    void markEmailAsOpened(Long sendId);
    void trackLinkClick(String trackingLink);
    
    // Email Templates
    EmailTemplate createEmailTemplate(String nombre, String asunto, String contenidoHtml, String categoria);
    Optional<EmailTemplate> getTemplateByName(String nombre);
    List<EmailTemplate> getTemplatesByCategory(String categoria);
    
    // Coupons & Discounts
    Coupon createCoupon(String codigo, String tipoDescuento, Double valor);
    Optional<Coupon> validateCoupon(String codigo);
    void applyCoupon(Usuario usuario, String codigo, Long pedidoId);
    List<Coupon> getActiveCoupons();
    
    // Loyalty
    void addLoyaltyPoints(Usuario usuario, Integer puntos, String razon);
    void redeemLoyaltyPoints(Usuario usuario, Integer puntos);
    Optional<LoyaltyPoints> getLoyaltyPoints(Usuario usuario);
    
    // Engagement
    void updateEngagementScore(Usuario usuario);
    Optional<UserEngagementTracking> getEngagementTracking(Usuario usuario);
}
