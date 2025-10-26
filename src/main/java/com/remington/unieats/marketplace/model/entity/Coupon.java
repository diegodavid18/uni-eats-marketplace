package com.remington.unieats.marketplace.model.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "coupons")
public class Coupon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String codigo;

    @Column(columnDefinition = "TEXT")
    private String descripcion;

    @Column(name = "tipo_descuento", length = 20)
    private String tipoDescuento;

    @Column(name = "valor_descuento", precision = 10, scale = 2, nullable = false)
    private BigDecimal valorDescuento;

    @Column(name = "cantidad_maxima")
    private Integer cantidadMaxima;

    @Column(name = "cantidad_usada")
    private Integer cantidadUsada = 0;

    @Column(name = "minimo_compra", precision = 10, scale = 2)
    private BigDecimal minimoCompra = BigDecimal.ZERO;

    @Column(name = "fecha_inicio")
    private LocalDateTime fechaInicio;

    @Column(name = "fecha_expiracion")
    private LocalDateTime fechaExpiracion;

    @Column(name = "tiendas_aplicables", columnDefinition = "TEXT")
    private String tiendasAplicables;

    @Column(name = "usuarios_aplicables", columnDefinition = "TEXT")
    private String usuariosAplicables;

    @Column(columnDefinition = "boolean default true")
    private Boolean activo = true;

    @Column(name = "created_by")
    private Long createdBy;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    public Coupon() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getCodigo() { return codigo; }
    public void setCodigo(String codigo) { this.codigo = codigo; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public String getTipoDescuento() { return tipoDescuento; }
    public void setTipoDescuento(String tipoDescuento) { this.tipoDescuento = tipoDescuento; }

    public BigDecimal getValorDescuento() { return valorDescuento; }
    public void setValorDescuento(BigDecimal valorDescuento) { this.valorDescuento = valorDescuento; }

    public Integer getCantidadMaxima() { return cantidadMaxima; }
    public void setCantidadMaxima(Integer cantidadMaxima) { this.cantidadMaxima = cantidadMaxima; }

    public Integer getCantidadUsada() { return cantidadUsada; }
    public void setCantidadUsada(Integer cantidadUsada) { this.cantidadUsada = cantidadUsada; }

    public BigDecimal getMinimoCompra() { return minimoCompra; }
    public void setMinimoCompra(BigDecimal minimoCompra) { this.minimoCompra = minimoCompra; }

    public LocalDateTime getFechaInicio() { return fechaInicio; }
    public void setFechaInicio(LocalDateTime fechaInicio) { this.fechaInicio = fechaInicio; }

    public LocalDateTime getFechaExpiracion() { return fechaExpiracion; }
    public void setFechaExpiracion(LocalDateTime fechaExpiracion) { this.fechaExpiracion = fechaExpiracion; }

    public String getTiendasAplicables() { return tiendasAplicables; }
    public void setTiendasAplicables(String tiendasAplicables) { this.tiendasAplicables = tiendasAplicables; }

    public String getUsuariosAplicables() { return usuariosAplicables; }
    public void setUsuariosAplicables(String usuariosAplicables) { this.usuariosAplicables = usuariosAplicables; }

    public Boolean getActivo() { return activo; }
    public void setActivo(Boolean activo) { this.activo = activo; }

    public Long getCreatedBy() { return createdBy; }
    public void setCreatedBy(Long createdBy) { this.createdBy = createdBy; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
