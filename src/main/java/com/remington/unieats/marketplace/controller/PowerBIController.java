package com.remington.unieats.marketplace.controller;

import com.remington.unieats.marketplace.config.PowerBIConfig;
import com.remington.unieats.marketplace.service.PowerBIService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/analytics")
public class PowerBIController {

    private static final Logger logger = LoggerFactory.getLogger(PowerBIController.class);

    @Autowired
    private PowerBIService powerBIService;
    
    @Autowired
    private PowerBIConfig powerBIConfig;

    /**
     * Página principal de analytics con Power BI
     */
    @GetMapping
    public String analyticsPage(Model model) {
        try {
            // Verificar si la configuración está completa
            if (!powerBIService.isConfigurationValid()) {
                model.addAttribute("error", "La configuración de Power BI no está completa. " +
                    "Verifica las propiedades en application.properties");
                return "analytics/config-error";
            }

            // Obtener reportes disponibles si hay workspace configurado
            if (powerBIConfig.getWorkspaceId() != null && !powerBIConfig.getWorkspaceId().isEmpty()) {
                Map<String, Object> workspaceInfo = powerBIService.getWorkspaceReports(powerBIConfig.getWorkspaceId());
                model.addAttribute("reports", workspaceInfo.get("reports"));
                model.addAttribute("reportsCount", workspaceInfo.get("count"));
            }

            model.addAttribute("workspaceId", powerBIConfig.getWorkspaceId());
            return "analytics/dashboard";
            
        } catch (Exception e) {
            logger.error("Error cargando página de analytics: ", e);
            model.addAttribute("error", "Error al conectar con Power BI: " + e.getMessage());
            return "analytics/error";
        }
    }

    /**
     * Endpoint para obtener embed token de un reporte específico
     */
    @GetMapping("/embed-token/{reportId}")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> getEmbedToken(@PathVariable String reportId) {
        try {
            String workspaceId = powerBIConfig.getWorkspaceId();
            if (workspaceId == null || workspaceId.isEmpty()) {
                Map<String, Object> error = new HashMap<>();
                error.put("error", "Workspace ID no configurado");
                return ResponseEntity.badRequest().body(error);
            }

            Map<String, Object> embedInfo = powerBIService.getEmbedToken(reportId, workspaceId);
            return ResponseEntity.ok(embedInfo);
            
        } catch (Exception e) {
            logger.error("Error obteniendo embed token para reporte {}: ", reportId, e);
            Map<String, Object> error = new HashMap<>();
            error.put("error", "Error al obtener token de embed: " + e.getMessage());
            return ResponseEntity.internalServerError().body(error);
        }
    }

    /**
     * Página específica para mostrar un reporte
     */
    @GetMapping("/report/{reportId}")
    public String showReport(@PathVariable String reportId, Model model) {
        try {
            String workspaceId = powerBIConfig.getWorkspaceId();
            if (workspaceId == null || workspaceId.isEmpty()) {
                model.addAttribute("error", "Workspace ID no configurado");
                return "analytics/error";
            }

            Map<String, Object> embedInfo = powerBIService.getEmbedToken(reportId, workspaceId);
            
            model.addAttribute("embedToken", embedInfo.get("embedToken"));
            model.addAttribute("embedUrl", embedInfo.get("embedUrl"));
            model.addAttribute("reportId", reportId);
            model.addAttribute("workspaceId", workspaceId);
            
            return "analytics/report";
            
        } catch (Exception e) {
            logger.error("Error cargando reporte {}: ", reportId, e);
            model.addAttribute("error", "Error al cargar el reporte: " + e.getMessage());
            return "analytics/error";
        }
    }

    /**
     * API para obtener lista de reportes
     */
    @GetMapping("/api/reports")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> getReports() {
        try {
            String workspaceId = powerBIConfig.getWorkspaceId();
            if (workspaceId == null || workspaceId.isEmpty()) {
                Map<String, Object> error = new HashMap<>();
                error.put("error", "Workspace ID no configurado");
                return ResponseEntity.badRequest().body(error);
            }

            Map<String, Object> workspaceInfo = powerBIService.getWorkspaceReports(workspaceId);
            return ResponseEntity.ok(workspaceInfo);
            
        } catch (Exception e) {
            logger.error("Error obteniendo lista de reportes: ", e);
            Map<String, Object> error = new HashMap<>();
            error.put("error", "Error al obtener reportes: " + e.getMessage());
            return ResponseEntity.internalServerError().body(error);
        }
    }

    /**
     * Página de configuración y testing
     */
    @GetMapping("/config")
    public String configPage(Model model) {
        model.addAttribute("isConfigured", powerBIService.isConfigurationValid());
        model.addAttribute("clientId", powerBIConfig.getClientId() != null ? "Configurado" : "No configurado");
        model.addAttribute("tenantId", powerBIConfig.getTenantId() != null ? "Configurado" : "No configurado");
        model.addAttribute("workspaceId", powerBIConfig.getWorkspaceId());
        return "analytics/config";
    }
}