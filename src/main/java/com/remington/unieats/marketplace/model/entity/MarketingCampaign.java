package com.remington.unieats.marketplace.model.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "marketing_campaigns")
public class MarketingCampaign {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 200)
    private String nombre;

    @Column(columnDefinition = "TEXT")
    private String descripcion;

    @Column(length = 50)
    private String tipo;

    @Column(length = 50)
    private String estado;

    @ManyToOne
    @JoinColumn(name = "segmento_id")
    private CustomerSegment segmento;

    @Column(name = "fecha_inicio")
    private LocalDateTime fechaInicio;

    @Column(name = "fecha_fin")
    private LocalDateTime fechaFin;

    @Column(precision = 10, scale = 2)
    private BigDecimal presupuesto;

    @Column(name = "presupuesto_gastado", precision = 10, scale = 2)
    private BigDecimal presupuestoGastado = BigDecimal.ZERO;

    @Column(name = "objetivo_conversion", precision = 5, scale = 2)
    private BigDecimal objetivoConversion;

    @Column(name = "tasa_conversion_actual", precision = 5, scale = 2)
    private BigDecimal tasaConversionActual = BigDecimal.ZERO;

    @Column(name = "created_by")
    private Long createdBy;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at")
    private LocalDateTime updatedAt = LocalDateTime.now();

    public MarketingCampaign() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public CustomerSegment getSegmento() { return segmento; }
    public void setSegmento(CustomerSegment segmento) { this.segmento = segmento; }

    public LocalDateTime getFechaInicio() { return fechaInicio; }
    public void setFechaInicio(LocalDateTime fechaInicio) { this.fechaInicio = fechaInicio; }

    public LocalDateTime getFechaFin() { return fechaFin; }
    public void setFechaFin(LocalDateTime fechaFin) { this.fechaFin = fechaFin; }

    public BigDecimal getPresupuesto() { return presupuesto; }
    public void setPresupuesto(BigDecimal presupuesto) { this.presupuesto = presupuesto; }

    public BigDecimal getPresupuestoGastado() { return presupuestoGastado; }
    public void setPresupuestoGastado(BigDecimal presupuestoGastado) { this.presupuestoGastado = presupuestoGastado; }

    public BigDecimal getObjetivoConversion() { return objetivoConversion; }
    public void setObjetivoConversion(BigDecimal objetivoConversion) { this.objetivoConversion = objetivoConversion; }

    public BigDecimal getTasaConversionActual() { return tasaConversionActual; }
    public void setTasaConversionActual(BigDecimal tasaConversionActual) { this.tasaConversionActual = tasaConversionActual; }

    public Long getCreatedBy() { return createdBy; }
    public void setCreatedBy(Long createdBy) { this.createdBy = createdBy; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
