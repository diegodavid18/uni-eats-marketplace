package com.remington.unieats.marketplace.controller;

import com.remington.unieats.marketplace.service.AIService;
import com.remington.unieats.marketplace.service.MarketingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Controller para endpoints de marketing con IA
 * Proporciona acceso a recomendaciones, estrategias y análisis generados por IA
 */
@RestController
@RequestMapping("/api/marketing")
@CrossOrigin(origins = "*")
public class MarketingController {

    private static final Logger log = LoggerFactory.getLogger(MarketingController.class);

    @Autowired
    private MarketingService marketingService;

    @Autowired
    private AIService aiService;

    // ==================== RECOMENDACIONES ====================

    /**
     * GET /api/marketing/recomendacion/producto
     * Genera recomendación para un producto específico
     * 
     * Query params:
     * - nombreProducto: Nombre del producto
     * - categoria: Categoría del producto
     * - tienda: Nombre de la tienda
     * - preferencias: Preferencias del usuario
     */
    @GetMapping("/recomendacion/producto")
    public ResponseEntity<?> generarRecomendacionProducto(
            @RequestParam String nombreProducto,
            @RequestParam String categoria,
            @RequestParam String tienda,
            @RequestParam(defaultValue = "Usuario interesado en variedad") String preferencias) {
        
        log.info("🎯 Generando recomendación para producto: {}", nombreProducto);
        
        try {
            String recomendacion = aiService.generateProductRecommendation(
                nombreProducto, categoria, tienda, preferencias
            );
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("producto", nombreProducto);
            response.put("tienda", tienda);
            response.put("recomendacion", recomendacion);
            response.put("timestamp", System.currentTimeMillis());
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("❌ Error generando recomendación: {}", e.getMessage());
            return ResponseEntity.status(500).body(Map.of(
                "success", false,
                "error", e.getMessage()
            ));
        }
    }

    /**
     * POST /api/marketing/recomendaciones/multiples
     * Genera múltiples recomendaciones para productos
     */
    @PostMapping("/recomendaciones/multiples")
    public ResponseEntity<?> generarMultiplesRecomendaciones(
            @RequestBody Map<String, Object> request) {
        
        try {
            List<Map<String, String>> productos = (List<Map<String, String>>) request.get("productos");
            String preferencias = (String) request.getOrDefault("preferencias", "Variedad");
            
            List<MarketingService.RecomendacionProducto> recomendaciones = new java.util.ArrayList<>();
            
            for (Map<String, String> producto : productos) {
                String recomendacion = aiService.generateProductRecommendation(
                    producto.get("nombre"),
                    producto.get("categoria"),
                    producto.get("tienda"),
                    preferencias
                );
                
                recomendaciones.add(new MarketingService.RecomendacionProducto(
                    Long.parseLong(producto.getOrDefault("id", "0")),
                    producto.get("nombre"),
                    producto.get("tienda"),
                    recomendacion,
                    Math.random() * 0.3 + 0.7
                ));
            }
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("cantidad", recomendaciones.size());
            response.put("recomendaciones", recomendaciones);
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("❌ Error generando recomendaciones múltiples: {}", e.getMessage());
            return ResponseEntity.status(500).body(Map.of(
                "success", false,
                "error", e.getMessage()
            ));
        }
    }

    // ==================== ESTRATEGIAS DE MARKETING ====================

    /**
     * GET /api/marketing/estrategia/vendedor
     * Genera estrategia de marketing para un vendedor
     * 
     * Query params:
     * - tiendaId: ID de la tienda
     * - nombreTienda: Nombre de la tienda
     * - categoriaPrincipal: Categoría principal de productos
     * - ventasDelMes: Número de ventas del mes
     * - ventaDiaria: Venta promedio diaria
     * - clientesActivos: Clientes activos
     */
    @GetMapping("/estrategia/vendedor")
    public ResponseEntity<?> generarEstrategiaVendedor(
            @RequestParam Long tiendaId,
            @RequestParam String nombreTienda,
            @RequestParam String categoriaPrincipal,
            @RequestParam(defaultValue = "100") Integer ventasDelMes,
            @RequestParam(defaultValue = "5.0") Double ventaDiaria,
            @RequestParam(defaultValue = "50") Integer clientesActivos) {
        
        log.info("📊 Generando estrategia para tienda: {}", nombreTienda);
        
        try {
            MarketingService.VentasInfo ventasInfo = new MarketingService.VentasInfo(
                ventasDelMes, ventaDiaria, clientesActivos
            );
            
            String estrategia = aiService.generateMarketingStrategy(
                nombreTienda,
                categoriaPrincipal,
                String.format("Ventas: %d/mes, %.2f/día, Clientes: %d", 
                    ventasDelMes, ventaDiaria, clientesActivos),
                "Estudiantes universitarios"
            );
            
            String tendencias = aiService.analyzeTrends(categoriaPrincipal);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("tiendaId", tiendaId);
            response.put("nombreTienda", nombreTienda);
            response.put("estrategia", estrategia);
            response.put("tendencias", tendencias);
            response.put("timestamp", System.currentTimeMillis());
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("❌ Error generando estrategia: {}", e.getMessage());
            return ResponseEntity.status(500).body(Map.of(
                "success", false,
                "error", e.getMessage()
            ));
        }
    }

    // ==================== ANÁLISIS Y TENDENCIAS ====================

    /**
     * GET /api/marketing/tendencias/categoria
     * Analiza tendencias de una categoría
     * 
     * Query params:
     * - nombreCategoria: Nombre de la categoría
     */
    @GetMapping("/tendencias/categoria")
    public ResponseEntity<?> analizarTendencias(
            @RequestParam String nombreCategoria) {
        
        log.info("📈 Analizando tendencias para: {}", nombreCategoria);
        
        try {
            MarketingService.AnalisisTendencias analisis = marketingService.analizarTendenciasCategoria(
                nombreCategoria
            );
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("categoria", analisis.categoria);
            response.put("analisis", analisis.analisis);
            response.put("oportunidades", analisis.oportunidades);
            response.put("fecha", analisis.fecha);
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("❌ Error analizando tendencias: {}", e.getMessage());
            return ResponseEntity.status(500).body(Map.of(
                "success", false,
                "error", e.getMessage()
            ));
        }
    }

    // ==================== CONTENIDO PROMOCIONAL ====================

    /**
     * POST /api/marketing/promocion/generar
     * Genera contenido promocional automático
     */
    @PostMapping("/promocion/generar")
    public ResponseEntity<?> generarPromocion(
            @RequestBody Map<String, String> request) {
        
        try {
            String nombreProducto = request.get("nombreProducto");
            String descuento = request.getOrDefault("descuento", "20%");
            String razon = request.getOrDefault("razon", "Promoción especial");
            
            log.info("🎉 Generando promoción para: {} ({})", nombreProducto, descuento);
            
            MarketingService.ContenidoPromocional contenido = marketingService.generarContenidoPromocional(
                nombreProducto, descuento, razon
            );
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("producto", contenido.producto);
            response.put("contenido", contenido.contenido);
            response.put("descuento", contenido.descuento);
            response.put("razon", contenido.razon);
            response.put("timestamp", contenido.fechaGeneracion);
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("❌ Error generando promoción: {}", e.getMessage());
            return ResponseEntity.status(500).body(Map.of(
                "success", false,
                "error", e.getMessage()
            ));
        }
    }

    /**
     * POST /api/marketing/promocion/variaciones
     * Genera múltiples variaciones de una promoción
     */
    @PostMapping("/promocion/variaciones")
    public ResponseEntity<?> generarVariacionesPromocion(
            @RequestBody Map<String, Object> request) {
        
        try {
            String nombreProducto = (String) request.get("nombreProducto");
            String descuento = (String) request.getOrDefault("descuento", "20%");
            String razon = (String) request.getOrDefault("razon", "Promoción especial");
            Integer numeroVariaciones = (Integer) request.getOrDefault("numeroVariaciones", 3);
            
            log.info("🎉 Generando {} variaciones para: {}", numeroVariaciones, nombreProducto);
            
            List<MarketingService.ContenidoPromocional> variaciones = 
                marketingService.generarVariacionesPromocion(
                    nombreProducto, descuento, razon, numeroVariaciones
                );
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("producto", nombreProducto);
            response.put("cantidad", variaciones.size());
            response.put("variaciones", variaciones);
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("❌ Error generando variaciones: {}", e.getMessage());
            return ResponseEntity.status(500).body(Map.of(
                "success", false,
                "error", e.getMessage()
            ));
        }
    }

    // ==================== HEALTH CHECK ====================

    /**
     * GET /api/marketing/health
     * Verifica el estado del servicio de IA
     */
    @GetMapping("/health")
    public ResponseEntity<?> health() {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "OK");
        response.put("service", "Marketing AI Service");
        response.put("timestamp", System.currentTimeMillis());
        response.put("features", new String[]{
            "Recomendaciones de productos",
            "Estrategias de marketing para vendedores",
            "Análisis de tendencias",
            "Generación de contenido promocional"
        });
        
        return ResponseEntity.ok(response);
    }

    /**
     * GET /api/marketing/test
     * Endpoint de prueba rápida
     */
    @GetMapping("/test")
    public ResponseEntity<?> testAI() {
        log.info("🧪 Test rápido de IA");
        
        try {
            String resultado = aiService.generateProductRecommendation(
                "Tacos al Pastor",
                "Comida Rápida",
                "La Taquería",
                "Usuario que ama comida mexicana y precios bajos"
            );
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("test", "Recomendación de producto");
            response.put("resultado", resultado);
            response.put("timestamp", System.currentTimeMillis());
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("❌ Test fallido: {}", e.getMessage());
            return ResponseEntity.status(500).body(Map.of(
                "success", false,
                "error", e.getMessage()
            ));
        }
    }
}
