package com.remington.unieats.marketplace.service;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * Servicio para integración con Hugging Face Inference API
 * Maneja todas las llamadas a modelos de IA alojados en Hugging Face
 */
@Service
public class AIService {

    private static final Logger log = LoggerFactory.getLogger(AIService.class);

    @Value("${huggingface.api-token}")
    private String huggingfaceToken;

    @Value("${huggingface.api-url}")
    private String huggingfaceApiUrl;

    @Value("${huggingface.marketing-model}")
    private String marketingModel;

    @Value("${huggingface.model-timeout:30000}")
    private long modelTimeout;

    @Value("${app.ai.cache.enabled:true}")
    private boolean cacheEnabled;

    @Value("${app.ai.cache.ttl-minutes:60}")
    private int cacheTtlMinutes;

    @Value("${app.ai.max-response-length:1024}")
    private int maxResponseLength;

    @Value("${app.ai.temperature:0.7}")
    private double temperature;

    @Value("${app.ai.top-p:0.9}")
    private double topP;

    private final Gson gson = new Gson();
    private final ConcurrentHashMap<String, CachedResponse> responseCache = new ConcurrentHashMap<>();

    /**
     * Genera recomendaciones de productos basadas en preferencias del usuario
     * 
     * @param productName Nombre del producto
     * @param categoryName Categoría del producto
     * @param storeName Nombre de la tienda
     * @param userPreferences Preferencias del usuario
     * @return Recomendación generada por IA
     */
    public String generateProductRecommendation(String productName, String categoryName, 
                                                String storeName, String userPreferences) {
        String prompt = String.format(
            "Como experto en marketing, analiza el producto '%s' de la categoría '%s' vendido por '%s'. " +
            "Considerando las preferencias del usuario: %s. " +
            "Genera una recomendación corta y persuasiva (máximo 2 oraciones) para este usuario. " +
            "Usa emojis relevantes para hacer más atractiva la recomendación.",
            productName, categoryName, storeName, userPreferences
        );
        
        return invokeModel(prompt, "product-recommendation-" + productName.hashCode());
    }

    /**
     * Genera estrategias de marketing para vendedores
     * 
     * @param storeName Nombre de la tienda
     * @param productCategory Categoría de productos vendidos
     * @param currentSales Ventas actuales (descripción)
     * @param targetAudience Audiencia objetivo
     * @return Estrategia de marketing generada por IA
     */
    public String generateMarketingStrategy(String storeName, String productCategory, 
                                           String currentSales, String targetAudience) {
        String prompt = String.format(
            "Eres un experto en marketing digital. Proporciona 3 estrategias específicas y accionables " +
            "para mejorar las ventas de '%s' que vende '%s'. " +
            "Contexto actual: %s. Audiencia objetivo: %s. " +
            "Incluye tácticas específicas, promociones y recomendaciones de contenido para redes sociales. " +
            "Sé conciso pero detallado.",
            storeName, productCategory, currentSales, targetAudience
        );
        
        return invokeModel(prompt, "marketing-strategy-" + storeName.hashCode());
    }

    /**
     * Genera análisis de tendencias de mercado
     * 
     * @param categoryName Categoría a analizar
     * @return Análisis de tendencias generado por IA
     */
    public String analyzeTrends(String categoryName) {
        String prompt = String.format(
            "Analiza las tendencias actuales en la categoría '%s' de comida universitaria. " +
            "Menciona: 1) Principales preferencias, 2) Oportunidades de negocio, 3) Diferenciadores clave. " +
            "Sé conciso y enfocado.",
            categoryName
        );
        
        return invokeModel(prompt, "trend-analysis-" + categoryName.hashCode());
    }

    /**
     * Genera contenido para promociones
     * 
     * @param productName Nombre del producto
     * @param discount Descuento ofrecido
     * @param reason Razón de la promoción
     * @return Contenido promocional generado por IA
     */
    public String generatePromotionalContent(String productName, String discount, String reason) {
        String prompt = String.format(
            "Crea un texto promocional atractivo de máximo 3 líneas para una promoción de '%s' " +
            "con descuento de %s. Razón: %s. " +
            "El texto debe ser persuasivo, incluir emojis relevantes y un call-to-action claro. " +
            "Apto para publicar en redes sociales.",
            productName, discount, reason
        );
        
        return invokeModel(prompt, "promo-content-" + productName.hashCode());
    }

    /**
     * Invoca el modelo de Hugging Face con manejo de cache y timeout
     * 
     * @param prompt Pregunta/instrucción para el modelo
     * @param cacheKey Clave para almacenar en cache
     * @return Respuesta del modelo
     */
    private String invokeModel(String prompt, String cacheKey) {
        try {
            // Verificar cache
            if (cacheEnabled && responseCache.containsKey(cacheKey)) {
                CachedResponse cached = responseCache.get(cacheKey);
                if (!cached.isExpired(cacheTtlMinutes)) {
                    log.info("✅ Respuesta obtenida del cache para: {}", cacheKey);
                    return cached.response;
                } else {
                    responseCache.remove(cacheKey);
                }
            }

            log.info("🤖 Invocando modelo Hugging Face: {} con prompt de {} caracteres", 
                    marketingModel, prompt.length());

            String url = huggingfaceApiUrl + "/" + marketingModel;
            
            try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
                HttpPost httpPost = new HttpPost(url);
                
                // Headers
                httpPost.setHeader("Authorization", "Bearer " + huggingfaceToken);
                httpPost.setHeader("Content-Type", "application/json");
                
                // Body
                JsonObject requestBody = new JsonObject();
                requestBody.addProperty("inputs", prompt);
                requestBody.addProperty("parameters", "{}");
                
                StringEntity entity = new StringEntity(
                    requestBody.toString(), 
                    ContentType.APPLICATION_JSON
                );
                httpPost.setEntity(entity);
                
                log.debug("📤 Request URL: {}", url);
                
                try (CloseableHttpResponse response = httpClient.execute(httpPost)) {
                    int statusCode = response.getCode();
                    
                    if (statusCode == 200) {
                        String responseText = readResponse(response);
                        
                        // Parsear respuesta
                        String extractedResponse = extractResponse(responseText);
                        
                        // Guardar en cache
                        if (cacheEnabled) {
                            responseCache.put(cacheKey, new CachedResponse(extractedResponse));
                            log.info("💾 Respuesta cacheada para: {}", cacheKey);
                        }
                        
                        log.info("✅ Respuesta exitosa de Hugging Face ({} caracteres)", 
                                extractedResponse.length());
                        return truncateResponse(extractedResponse);
                        
                    } else if (statusCode == 429) {
                        log.warn("⏱️ Rate limit de Hugging Face - reintentando en 5 segundos");
                        Thread.sleep(5000);
                        return invokeModel(prompt, cacheKey); // Reintentar
                        
                    } else if (statusCode == 503) {
                        log.warn("⚠️ Modelo de Hugging Face cargándose - esperando...");
                        Thread.sleep(10000);
                        return invokeModel(prompt, cacheKey); // Reintentar
                        
                    } else {
                        String errorResponse = readResponse(response);
                        log.error("❌ Error en Hugging Face [{}]: {}", statusCode, errorResponse);
                        return generateFallbackResponse(prompt);
                    }
                }
            }
        } catch (InterruptedException e) {
            log.error("❌ Interrupción en llamada a IA: {}", e.getMessage());
            Thread.currentThread().interrupt();
            return generateFallbackResponse(prompt);
        } catch (Exception e) {
            log.error("❌ Error invocando modelo Hugging Face: {}", e.getMessage(), e);
            return generateFallbackResponse(prompt);
        }
    }

    /**
     * Lee la respuesta HTTP
     */
    private String readResponse(CloseableHttpResponse response) throws Exception {
        StringBuilder result = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(response.getEntity().getContent()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                result.append(line);
            }
        }
        return result.toString();
    }

    /**
     * Extrae el texto de la respuesta del modelo
     */
    private String extractResponse(String jsonResponse) {
        try {
            // La respuesta viene como array de objetos con "generated_text"
            JsonObject[] responses = gson.fromJson(jsonResponse, JsonObject[].class);
            if (responses != null && responses.length > 0) {
                String generated = responses[0].get("generated_text").getAsString();
                // El modelo incluye el prompt en la respuesta, extraer solo la parte nueva
                return generated.trim();
            }
        } catch (Exception e) {
            log.warn("⚠️ No se pudo parsear respuesta JSON: {}", e.getMessage());
        }
        return jsonResponse;
    }

    /**
     * Trunca la respuesta si excede el límite configurado
     */
    private String truncateResponse(String response) {
        if (response.length() > maxResponseLength) {
            return response.substring(0, maxResponseLength) + "...";
        }
        return response;
    }

    /**
     * Genera respuesta de fallback cuando no funciona la IA
     */
    private String generateFallbackResponse(String prompt) {
        log.warn("⚠️ Usando respuesta de fallback para prompt: {}", prompt.substring(0, 50));
        
        if (prompt.contains("recomendación") || prompt.contains("producto")) {
            return "🎯 Producto destacado - Esta opción ofrece excelente relación calidad-precio. " +
                   "¡Pruébalo hoy y disfruta de ofertas especiales para estudiantes!";
        } else if (prompt.contains("estrategia") || prompt.contains("marketing")) {
            return "📊 Estrategias recomendadas:\n" +
                   "1. Aumenta visibilidad en redes sociales\n" +
                   "2. Ofrece promociones para estudiantes\n" +
                   "3. Crea contenido de valor en tu perfil";
        } else if (prompt.contains("tendencia") || prompt.contains("análisis")) {
            return "📈 Las tendencias muestran crecimiento en productos rápidos y saludables. " +
                   "Considera diversificar tu oferta.";
        } else if (prompt.contains("promoción") || prompt.contains("promoci")) {
            return "🎉 ¡OFERTA ESPECIAL! Aprovecha esta promoción y ahorra hoy. " +
                   "¡Solo disponible por tiempo limitado!";
        }
        
        return "✨ Descubre nuevas oportunidades para crecer tu negocio. Contacta con soporte para más.";
    }

    /**
     * Clase para almacenar respuestas en cache con timestamp
     */
    private static class CachedResponse {
        String response;
        long timestamp;

        CachedResponse(String response) {
            this.response = response;
            this.timestamp = System.currentTimeMillis();
        }

        boolean isExpired(int ttlMinutes) {
            long expirationTime = TimeUnit.MINUTES.toMillis(ttlMinutes);
            return System.currentTimeMillis() - timestamp > expirationTime;
        }
    }
}
