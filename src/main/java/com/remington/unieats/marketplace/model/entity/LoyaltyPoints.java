package com.remington.unieats.marketplace.model.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "loyalty_points")
public class LoyaltyPoints {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "usuario_id", nullable = false, unique = true)
    private Usuario usuario;

    @Column(name = "puntos_totales")
    private Integer puntosTotales = 0;

    @Column(name = "puntos_disponibles")
    private Integer puntosDisponibles = 0;

    @Column(name = "puntos_canjeados")
    private Integer puntosCanjeados = 0;

    @Column(name = "nivel_tier", length = 50)
    private String nivelTier = "BRONZE";

    @Column(name = "fecha_proximo_nivel")
    private LocalDateTime fechaProximoNivel;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at")
    private LocalDateTime updatedAt = LocalDateTime.now();

    public LoyaltyPoints() {}

    public LoyaltyPoints(Usuario usuario) {
        this.usuario = usuario;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }

    public Integer getPuntosTotales() { return puntosTotales; }
    public void setPuntosTotales(Integer puntosTotales) { this.puntosTotales = puntosTotales; }

    public Integer getPuntosDisponibles() { return puntosDisponibles; }
    public void setPuntosDisponibles(Integer puntosDisponibles) { this.puntosDisponibles = puntosDisponibles; }

    public Integer getPuntosCanjeados() { return puntosCanjeados; }
    public void setPuntosCanjeados(Integer puntosCanjeados) { this.puntosCanjeados = puntosCanjeados; }

    public String getNivelTier() { return nivelTier; }
    public void setNivelTier(String nivelTier) { this.nivelTier = nivelTier; }

    public LocalDateTime getFechaProximoNivel() { return fechaProximoNivel; }
    public void setFechaProximoNivel(LocalDateTime fechaProximoNivel) { this.fechaProximoNivel = fechaProximoNivel; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
