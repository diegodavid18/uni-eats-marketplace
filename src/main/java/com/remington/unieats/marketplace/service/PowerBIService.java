package com.remington.unieats.marketplace.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microsoft.aad.msal4j.ClientCredentialFactory;
import com.microsoft.aad.msal4j.ClientCredentialParameters;
import com.microsoft.aad.msal4j.ConfidentialClientApplication;
import com.microsoft.aad.msal4j.IAuthenticationResult;
import com.remington.unieats.marketplace.config.PowerBIConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Service
public class PowerBIService {

    private static final Logger logger = LoggerFactory.getLogger(PowerBIService.class);
    
    @Autowired
    private PowerBIConfig powerBIConfig;
    
    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();
    
    /**
     * Obtiene el token de acceso para Power BI API
     */
    public String getAccessToken() {
        try {
            ConfidentialClientApplication app = ConfidentialClientApplication.builder(
                    powerBIConfig.getClientId(),
                    ClientCredentialFactory.createFromSecret(powerBIConfig.getClientSecret()))
                    .authority(powerBIConfig.getAuthorityUri())
                    .build();

            ClientCredentialParameters clientCredentialParam = ClientCredentialParameters.builder(
                    Collections.singleton(powerBIConfig.getScope()))
                    .build();

            CompletableFuture<IAuthenticationResult> future = app.acquireToken(clientCredentialParam);
            IAuthenticationResult result = future.get();
            
            return result.accessToken();
        } catch (Exception e) {
            logger.error("Error obteniendo token de Power BI: ", e);
            throw new RuntimeException("Error al autenticar con Power BI", e);
        }
    }
    
    /**
     * Obtiene un embed token para un reporte específico
     */
    public Map<String, Object> getEmbedToken(String reportId, String workspaceId) {
        try {
            String accessToken = getAccessToken();
            String url = String.format("https://api.powerbi.com/v1.0/myorg/groups/%s/reports/%s/GenerateToken", 
                                     workspaceId, reportId);
            
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + accessToken);
            headers.set("Content-Type", "application/json");
            
            // Configuración del embed token
            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("accessLevel", "View");
            
            HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, request, String.class);
            
            JsonNode responseJson = objectMapper.readTree(response.getBody());
            
            Map<String, Object> embedInfo = new HashMap<>();
            embedInfo.put("embedToken", responseJson.get("token").asText());
            embedInfo.put("embedUrl", getReportEmbedUrl(reportId, workspaceId));
            embedInfo.put("reportId", reportId);
            embedInfo.put("workspaceId", workspaceId);
            
            return embedInfo;
        } catch (Exception e) {
            logger.error("Error obteniendo embed token: ", e);
            throw new RuntimeException("Error al obtener embed token de Power BI", e);
        }
    }
    
    /**
     * Obtiene la URL de embed para un reporte
     */
    private String getReportEmbedUrl(String reportId, String workspaceId) {
        try {
            String accessToken = getAccessToken();
            String url = String.format("https://api.powerbi.com/v1.0/myorg/groups/%s/reports/%s", 
                                     workspaceId, reportId);
            
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + accessToken);
            
            HttpEntity<String> request = new HttpEntity<>(headers);
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, request, String.class);
            
            JsonNode responseJson = objectMapper.readTree(response.getBody());
            return responseJson.get("embedUrl").asText();
        } catch (Exception e) {
            logger.error("Error obteniendo embed URL: ", e);
            throw new RuntimeException("Error al obtener URL de embed", e);
        }
    }
    
    /**
     * Obtiene información de todos los reportes del workspace
     */
    public Map<String, Object> getWorkspaceReports(String workspaceId) {
        try {
            String accessToken = getAccessToken();
            String url = String.format("https://api.powerbi.com/v1.0/myorg/groups/%s/reports", workspaceId);
            
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + accessToken);
            
            HttpEntity<String> request = new HttpEntity<>(headers);
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, request, String.class);
            
            JsonNode responseJson = objectMapper.readTree(response.getBody());
            
            Map<String, Object> result = new HashMap<>();
            result.put("reports", responseJson.get("value"));
            result.put("count", responseJson.get("value").size());
            
            return result;
        } catch (Exception e) {
            logger.error("Error obteniendo reportes del workspace: ", e);
            throw new RuntimeException("Error al obtener reportes del workspace", e);
        }
    }
    
    /**
     * Valida si la configuración de Power BI está completa
     */
    public boolean isConfigurationValid() {
        return powerBIConfig.getClientId() != null && !powerBIConfig.getClientId().isEmpty() &&
               powerBIConfig.getClientSecret() != null && !powerBIConfig.getClientSecret().isEmpty() &&
               powerBIConfig.getTenantId() != null && !powerBIConfig.getTenantId().isEmpty();
    }
}