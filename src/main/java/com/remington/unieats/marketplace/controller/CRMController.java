package com.remington.unieats.marketplace.controller;

import com.remington.unieats.marketplace.model.entity.*;
import com.remington.unieats.marketplace.service.CRMService;
import com.remington.unieats.marketplace.service.CRMServiceImpl;
import com.remington.unieats.marketplace.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;
import java.util.Map;
import java.util.HashMap;

@RestController
@RequestMapping("/api/crm")
public class CRMController {

    @Autowired private CRMService crmService;
    @Autowired private EmailService emailService;

    // ===== CUSTOMER PROFILES =====
    @PostMapping("/profiles/create/{usuarioId}")
    public ResponseEntity<?> createProfile(@PathVariable Integer usuarioId) {
        try {
            // Simulado - en producción obtener Usuario del repo
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Profile creation initiated");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/profiles/segment/{segment}")
    public ResponseEntity<List<CustomerProfile>> getProfilesBySegment(@PathVariable String segment) {
        return ResponseEntity.ok(crmService.getProfilesBySegment(segment));
    }

    @PutMapping("/profiles/{profileId}/segment")
    public ResponseEntity<?> updateSegment(@PathVariable Long profileId, @RequestParam String segment) {
        try {
            crmService.updateSegment(profileId, segment);
            return ResponseEntity.ok(Map.of("message", "Segment updated"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // ===== CAMPAIGNS =====
    @PostMapping("/campaigns")
    public ResponseEntity<MarketingCampaign> createCampaign(@RequestBody Map<String, String> payload) {
        MarketingCampaign campaign = crmService.createCampaign(
            payload.get("nombre"),
            payload.get("tipo"),
            payload.get("descripcion")
        );
        return ResponseEntity.ok(campaign);
    }

    @GetMapping("/campaigns/status/{status}")
    public ResponseEntity<List<MarketingCampaign>> getCampaignsByStatus(@PathVariable String status) {
        return ResponseEntity.ok(crmService.getCampaignsByStatus(status));
    }

    @PutMapping("/campaigns/{campaignId}/status")
    public ResponseEntity<?> updateCampaignStatus(@PathVariable Long campaignId, @RequestParam String status) {
        try {
            crmService.updateCampaignStatus(campaignId, status);
            return ResponseEntity.ok(Map.of("message", "Campaign status updated"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // ===== EMAIL TEMPLATES =====
    @PostMapping("/templates")
    public ResponseEntity<EmailTemplate> createTemplate(@RequestBody Map<String, String> payload) {
        EmailTemplate template = crmService.createEmailTemplate(
            payload.get("nombre"),
            payload.get("asunto"),
            payload.get("contenidoHtml") != null ? payload.get("contenidoHtml") : payload.get("contenido"),
            payload.get("categoria")
        );
        return ResponseEntity.ok(template);
    }

    @GetMapping("/templates/category/{categoria}")
    public ResponseEntity<List<EmailTemplate>> getTemplatesByCategory(@PathVariable String categoria) {
        return ResponseEntity.ok(crmService.getTemplatesByCategory(categoria));
    }

    // ===== COUPONS =====
    @PostMapping("/coupons")
    public ResponseEntity<Coupon> createCoupon(@RequestBody Map<String, Object> payload) {
        Coupon coupon = crmService.createCoupon(
            (String) payload.get("codigo"),
            (String) payload.get("tipo"),
            ((Number) payload.get("valor")).doubleValue()
        );
        return ResponseEntity.ok(coupon);
    }

    @GetMapping("/coupons/validate/{codigo}")
    public ResponseEntity<?> validateCoupon(@PathVariable String codigo) {
        Optional<Coupon> coupon = crmService.validateCoupon(codigo);
        if (coupon.isPresent()) {
            return ResponseEntity.ok(coupon.get());
        }
        return ResponseEntity.badRequest().body(Map.of("error", "Coupon not valid"));
    }

    @GetMapping("/coupons/active")
    public ResponseEntity<List<Coupon>> getActiveCoupons() {
        return ResponseEntity.ok(crmService.getActiveCoupons());
    }

    // ===== LOYALTY =====
    @PostMapping("/loyalty/add/{usuarioId}")
    public ResponseEntity<?> addLoyaltyPoints(@PathVariable Integer usuarioId, 
                                               @RequestParam Integer puntos, 
                                               @RequestParam String razon) {
        try {
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Loyalty points added");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/loyalty/{usuarioId}")
    public ResponseEntity<?> getLoyaltyPoints(@PathVariable Integer usuarioId) {
        try {
            Map<String, Object> response = new HashMap<>();
            response.put("puntos", 0);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // ===== ENGAGEMENT =====
    @GetMapping("/engagement/{usuarioId}")
    public ResponseEntity<?> getEngagement(@PathVariable Integer usuarioId) {
        try {
            Map<String, Object> response = new HashMap<>();
            response.put("score", 0);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // ===== CAMPAIGN SENDS =====
    @GetMapping("/campaigns/{campaignId}/stats")
    public ResponseEntity<List<CampaignSend>> getCampaignStats(@PathVariable Long campaignId) {
        return ResponseEntity.ok(crmService.getCampaignSendStats(campaignId));
    }

    // ===== CAMPAÑAS MASIVAS =====
    @PostMapping("/campaigns/{campaignId}/send-segment/{segment}/{templateId}")
    public ResponseEntity<?> enviarCampanaSegmento(
            @PathVariable Long campaignId,
            @PathVariable String segment,
            @PathVariable Long templateId) {
        try {
            int enviados = ((CRMServiceImpl) crmService).enviarCampanaPorSegmento(campaignId, segment, templateId);
            return ResponseEntity.ok(Map.of(
                "message", "Campaña enviada",
                "totalEnviados", enviados,
                "campaignId", campaignId,
                "segment", segment
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/campaigns/{campaignId}/analytics")
    public ResponseEntity<?> obtenerAnalyticsCampana(@PathVariable Long campaignId) {
        try {
            Map<String, Object> stats = ((CRMServiceImpl) crmService).obtenerEstadisticasCampana(campaignId);
            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/email/send-test")
    public ResponseEntity<?> enviarEmailTest(@RequestParam String destinatario) {
        try {
            boolean enviado = emailService.enviarEmailHtml(
                "Test - UniEats Marketplace",
                "<h1>¡Hola!</h1><p>Este es un correo de prueba desde UniEats Marketplace</p>",
                destinatario
            );
            return ResponseEntity.ok(Map.of(
                "message", enviado ? "Correo enviado exitosamente" : "Error al enviar",
                "destinatario", destinatario,
                "enviado", enviado
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}
