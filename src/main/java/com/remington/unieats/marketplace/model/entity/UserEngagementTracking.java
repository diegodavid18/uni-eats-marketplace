package com.remington.unieats.marketplace.model.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "user_engagement_tracking", indexes = {
    @Index(name = "idx_engagement_score", columnList = "engagement_score DESC")
})
public class UserEngagementTracking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "usuario_id", nullable = false, unique = true)
    private Usuario usuario;

    @Column(name = "ultima_visita")
    private LocalDateTime ultimaVisita;

    @Column(name = "dias_desde_compra")
    private Integer diasDesdeCompra;

    @Column(name = "email_abiertos_mes")
    private Integer emailAbiertoesMes = 0;

    @Column(name = "email_clics_mes")
    private Integer emailClicsMes = 0;

    @Column(name = "carrito_abandonado_veces")
    private Integer carritoAbandonadoVeces = 0;

    @Column(name = "reviews_escritos")
    private Integer reviewsEscritos = 0;

    @Column(name = "referidos_exitosos")
    private Integer referidosExitosos = 0;

    @Column(name = "engagement_score", precision = 5, scale = 2)
    private BigDecimal engagementScore = BigDecimal.ZERO;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt = LocalDateTime.now();

    public UserEngagementTracking() {}

    public UserEngagementTracking(Usuario usuario) {
        this.usuario = usuario;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }

    public LocalDateTime getUltimaVisita() { return ultimaVisita; }
    public void setUltimaVisita(LocalDateTime ultimaVisita) { this.ultimaVisita = ultimaVisita; }

    public Integer getDiasDesdeCompra() { return diasDesdeCompra; }
    public void setDiasDesdeCompra(Integer diasDesdeCompra) { this.diasDesdeCompra = diasDesdeCompra; }

    public Integer getEmailAbiertoesMes() { return emailAbiertoesMes; }
    public void setEmailAbiertoesMes(Integer emailAbiertoesMes) { this.emailAbiertoesMes = emailAbiertoesMes; }

    public Integer getEmailClicsMes() { return emailClicsMes; }
    public void setEmailClicsMes(Integer emailClicsMes) { this.emailClicsMes = emailClicsMes; }

    public Integer getCarritoAbandonadoVeces() { return carritoAbandonadoVeces; }
    public void setCarritoAbandonadoVeces(Integer carritoAbandonadoVeces) { this.carritoAbandonadoVeces = carritoAbandonadoVeces; }

    public Integer getReviewsEscritos() { return reviewsEscritos; }
    public void setReviewsEscritos(Integer reviewsEscritos) { this.reviewsEscritos = reviewsEscritos; }

    public Integer getReferidosExitosos() { return referidosExitosos; }
    public void setReferidosExitosos(Integer referidosExitosos) { this.referidosExitosos = referidosExitosos; }

    public BigDecimal getEngagementScore() { return engagementScore; }
    public void setEngagementScore(BigDecimal engagementScore) { this.engagementScore = engagementScore; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
