package com.remington.unieats.marketplace.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * Servicio de Analytics para IA
 * Registra, analiza y proporciona estadísticas sobre las respuestas generadas por IA
 */
@Service
public class AIAnalyticsService {

    private static final Logger log = LoggerFactory.getLogger(AIAnalyticsService.class);

    @Value("${app.ai.analytics.enabled:true}")
    private boolean analyticsEnabled;

    @Value("${app.ai.analytics.log-queries:true}")
    private boolean logQueries;

    // Almacenamiento en memoria de registros de IA
    private final List<AIQueryRecord> queryRecords = Collections.synchronizedList(new ArrayList<>());
    private final ConcurrentHashMap<String, Integer> featureUsageMap = new ConcurrentHashMap<>();

    /**
     * Registra una consulta a IA
     */
    public void registrarConsulta(String tipo, String prompt, String respuesta, long tiempoEjecucion) {
        if (!analyticsEnabled) {
            return;
        }

        try {
            AIQueryRecord record = new AIQueryRecord();
            record.setTipo(tipo);
            record.setPrompt(prompt);
            record.setRespuesta(respuesta);
            record.setTiempoEjecucionMs(tiempoEjecucion);
            record.setFecha(LocalDateTime.now());

            queryRecords.add(record);

            // Registrar uso de feature
            featureUsageMap.put(tipo, featureUsageMap.getOrDefault(tipo, 0) + 1);

            if (logQueries) {
                log.info("📊 IA Query Registrada - Tipo: {}, Tiempo: {}ms, Caracteres: {}",
                        tipo, tiempoEjecucion, respuesta.length());
            }
        } catch (Exception e) {
            log.error("❌ Error registrando consulta de IA: {}", e.getMessage());
        }
    }

    /**
     * Obtiene estadísticas generales
     */
    public EstadisticasIA obtenerEstadisticas() {
        if (queryRecords.isEmpty()) {
            return new EstadisticasIA();
        }

        long totalConsultas = queryRecords.size();
        long tiempoPromedio = (long) queryRecords.stream()
                .mapToLong(AIQueryRecord::getTiempoEjecucionMs)
                .average()
                .orElse(0);

        long tiempoMaximo = queryRecords.stream()
                .mapToLong(AIQueryRecord::getTiempoEjecucionMs)
                .max()
                .orElse(0);

        long tiempoMinimo = queryRecords.stream()
                .mapToLong(AIQueryRecord::getTiempoEjecucionMs)
                .min()
                .orElse(0);

        double longitudPromedio = queryRecords.stream()
                .mapToInt(r -> r.getRespuesta().length())
                .average()
                .orElse(0);

        EstadisticasIA stats = new EstadisticasIA();
        stats.setTotalConsultas(totalConsultas);
        stats.setTiempoPromedioMs(tiempoPromedio);
        stats.setTiempoMaximoMs(tiempoMaximo);
        stats.setTiempoMinimoMs(tiempoMinimo);
        stats.setLongitudPromedioCaracteres((int) longitudPromedio);
        stats.setUsosPorTipo(new HashMap<>(featureUsageMap));
        stats.setFechaUltimaConsulta(queryRecords.get(queryRecords.size() - 1).getFecha());

        return stats;
    }

    /**
     * Obtiene estadísticas por tipo de consulta
     */
    public Map<String, EstadisticasPorTipo> obtenerEstadisticasPorTipo() {
        return queryRecords.stream()
                .collect(Collectors.groupingBy(
                        AIQueryRecord::getTipo,
                        Collectors.teeing(
                                Collectors.counting(),
                                Collectors.averagingLong(AIQueryRecord::getTiempoEjecucionMs),
                                (count, tiempoPromedio) -> new EstadisticasPorTipo(
                                        count.intValue(),
                                        Math.round(tiempoPromedio)
                                )
                        )
                ));
    }

    /**
     * Obtiene el top de consultas más rápidas
     */
    public List<AIQueryRecord> obtenerConsultasRapidas(int limite) {
        return queryRecords.stream()
                .sorted(Comparator.comparingLong(AIQueryRecord::getTiempoEjecucionMs))
                .limit(limite)
                .collect(Collectors.toList());
    }

    /**
     * Obtiene el top de consultas más lentas
     */
    public List<AIQueryRecord> obtenerConsultasLentas(int limite) {
        return queryRecords.stream()
                .sorted(Comparator.comparingLong(AIQueryRecord::getTiempoEjecucionMs).reversed())
                .limit(limite)
                .collect(Collectors.toList());
    }

    /**
     * Obtiene registros por tipo específico
     */
    public List<AIQueryRecord> obtenerRegistrosPorTipo(String tipo) {
        return queryRecords.stream()
                .filter(r -> r.getTipo().equals(tipo))
                .collect(Collectors.toList());
    }

    /**
     * Obtiene registros dentro de un rango de tiempo
     */
    public List<AIQueryRecord> obtenerRegistrosPorFecha(LocalDateTime desde, LocalDateTime hasta) {
        return queryRecords.stream()
                .filter(r -> r.getFecha().isAfter(desde) && r.getFecha().isBefore(hasta))
                .collect(Collectors.toList());
    }

    /**
     * Genera reporte de performance
     */
    public ReportePerformance generarReportePerformance() {
        if (queryRecords.isEmpty()) {
            return new ReportePerformance();
        }

        ReportePerformance reporte = new ReportePerformance();
        reporte.setEstadisticasGenerales(obtenerEstadisticas());
        reporte.setEstadisticasPorTipo(obtenerEstadisticasPorTipo());
        reporte.setConsultasRapidas(obtenerConsultasRapidas(5));
        reporte.setConsultasLentas(obtenerConsultasLentas(5));

        // Calcular health score (0-100)
        long tiempoPromedio = reporte.getEstadisticasGenerales().getTiempoPromedioMs();
        double healthScore = Math.max(0, 100 - (tiempoPromedio / 100)); // Penalizar por tiempo
        reporte.setHealthScore(Math.min(100, healthScore));

        return reporte;
    }

    /**
     * Limpia registros antiguos (más de X horas)
     */
    public int limpiarRegistrosAntiguos(int horasAtrás) {
        LocalDateTime limite = LocalDateTime.now().minusHours(horasAtrás);
        int before = queryRecords.size();

        queryRecords.removeIf(r -> r.getFecha().isBefore(limite));

        int removed = before - queryRecords.size();
        log.info("🧹 Limpiados {} registros de IA más antiguos de {} horas", removed, horasAtrás);

        return removed;
    }

    /**
     * Exporta registros a CSV (formato string)
     */
    public String exportarACSV() {
        StringBuilder csv = new StringBuilder();
        csv.append("Tipo,Fecha,TiempoMs,LongitudRespuesta,Prompt\n");

        for (AIQueryRecord record : queryRecords) {
            csv.append(String.format("%s,%s,%d,%d,\"%s\"\n",
                    record.getTipo(),
                    record.getFecha(),
                    record.getTiempoEjecucionMs(),
                    record.getRespuesta().length(),
                    record.getPrompt().replaceAll("\"", "").substring(0, Math.min(50, record.getPrompt().length()))
            ));
        }

        return csv.toString();
    }

    // ==================== CLASES INTERNAS ====================

    public static class AIQueryRecord {
        private String tipo;
        private String prompt;
        private String respuesta;
        private long tiempoEjecucionMs;
        private LocalDateTime fecha;

        public String getTipo() { return tipo; }
        public void setTipo(String tipo) { this.tipo = tipo; }

        public String getPrompt() { return prompt; }
        public void setPrompt(String prompt) { this.prompt = prompt; }

        public String getRespuesta() { return respuesta; }
        public void setRespuesta(String respuesta) { this.respuesta = respuesta; }

        public long getTiempoEjecucionMs() { return tiempoEjecucionMs; }
        public void setTiempoEjecucionMs(long tiempoEjecucionMs) { this.tiempoEjecucionMs = tiempoEjecucionMs; }

        public LocalDateTime getFecha() { return fecha; }
        public void setFecha(LocalDateTime fecha) { this.fecha = fecha; }
    }

    public static class EstadisticasIA {
        private long totalConsultas;
        private long tiempoPromedioMs;
        private long tiempoMaximoMs;
        private long tiempoMinimoMs;
        private int longitudPromedioCaracteres;
        private Map<String, Integer> usosPorTipo;
        private LocalDateTime fechaUltimaConsulta;

        public long getTotalConsultas() { return totalConsultas; }
        public void setTotalConsultas(long totalConsultas) { this.totalConsultas = totalConsultas; }

        public long getTiempoPromedioMs() { return tiempoPromedioMs; }
        public void setTiempoPromedioMs(long tiempoPromedioMs) { this.tiempoPromedioMs = tiempoPromedioMs; }

        public long getTiempoMaximoMs() { return tiempoMaximoMs; }
        public void setTiempoMaximoMs(long tiempoMaximoMs) { this.tiempoMaximoMs = tiempoMaximoMs; }

        public long getTiempoMinimoMs() { return tiempoMinimoMs; }
        public void setTiempoMinimoMs(long tiempoMinimoMs) { this.tiempoMinimoMs = tiempoMinimoMs; }

        public int getLongitudPromedioCaracteres() { return longitudPromedioCaracteres; }
        public void setLongitudPromedioCaracteres(int longitudPromedioCaracteres) { 
            this.longitudPromedioCaracteres = longitudPromedioCaracteres; 
        }

        public Map<String, Integer> getUsosPorTipo() { return usosPorTipo; }
        public void setUsosPorTipo(Map<String, Integer> usosPorTipo) { this.usosPorTipo = usosPorTipo; }

        public LocalDateTime getFechaUltimaConsulta() { return fechaUltimaConsulta; }
        public void setFechaUltimaConsulta(LocalDateTime fechaUltimaConsulta) { 
            this.fechaUltimaConsulta = fechaUltimaConsulta; 
        }

        public EstadisticasIA() {
            this.totalConsultas = 0;
            this.tiempoPromedioMs = 0;
            this.tiempoMaximoMs = 0;
            this.tiempoMinimoMs = 0;
            this.longitudPromedioCaracteres = 0;
            this.usosPorTipo = new HashMap<>();
            this.fechaUltimaConsulta = LocalDateTime.now();
        }
    }

    public static class EstadisticasPorTipo {
        private int cantidad;
        private long tiempoPromedioMs;

        public int getCantidad() { return cantidad; }
        public long getTiempoPromedioMs() { return tiempoPromedioMs; }

        public EstadisticasPorTipo(int cantidad, long tiempoPromedioMs) {
            this.cantidad = cantidad;
            this.tiempoPromedioMs = tiempoPromedioMs;
        }
    }

    public static class ReportePerformance {
        private EstadisticasIA estadisticasGenerales;
        private Map<String, EstadisticasPorTipo> estadisticasPorTipo;
        private List<AIQueryRecord> consultasRapidas;
        private List<AIQueryRecord> consultasLentas;
        private double healthScore;

        public EstadisticasIA getEstadisticasGenerales() { return estadisticasGenerales; }
        public void setEstadisticasGenerales(EstadisticasIA estadisticasGenerales) { 
            this.estadisticasGenerales = estadisticasGenerales; 
        }

        public Map<String, EstadisticasPorTipo> getEstadisticasPorTipo() { return estadisticasPorTipo; }
        public void setEstadisticasPorTipo(Map<String, EstadisticasPorTipo> estadisticasPorTipo) { 
            this.estadisticasPorTipo = estadisticasPorTipo; 
        }

        public List<AIQueryRecord> getConsultasRapidas() { return consultasRapidas; }
        public void setConsultasRapidas(List<AIQueryRecord> consultasRapidas) { 
            this.consultasRapidas = consultasRapidas; 
        }

        public List<AIQueryRecord> getConsultasLentas() { return consultasLentas; }
        public void setConsultasLentas(List<AIQueryRecord> consultasLentas) { 
            this.consultasLentas = consultasLentas; 
        }

        public double getHealthScore() { return healthScore; }
        public void setHealthScore(double healthScore) { this.healthScore = healthScore; }

        public ReportePerformance() {
            this.estadisticasGenerales = new EstadisticasIA();
            this.estadisticasPorTipo = new HashMap<>();
            this.consultasRapidas = new ArrayList<>();
            this.consultasLentas = new ArrayList<>();
            this.healthScore = 100.0;
        }
    }
}
