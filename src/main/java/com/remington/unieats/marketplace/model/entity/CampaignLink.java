package com.remington.unieats.marketplace.model.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "campaign_links")
public class CampaignLink {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "campaign_id", nullable = false)
    private MarketingCampaign campaign;

    @Column(name = "enlace_original", length = 500, nullable = false)
    private String enlaceOriginal;

    @Column(name = "enlace_trackeable", length = 500, unique = true)
    private String enlaceTrackeable;

    @Column(length = 200)
    private String descripcion;

    @Column(name = "clics_total")
    private Integer clicsTotal = 0;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    public CampaignLink() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public MarketingCampaign getCampaign() { return campaign; }
    public void setCampaign(MarketingCampaign campaign) { this.campaign = campaign; }

    public String getEnlaceOriginal() { return enlaceOriginal; }
    public void setEnlaceOriginal(String enlaceOriginal) { this.enlaceOriginal = enlaceOriginal; }

    public String getEnlaceTrackeable() { return enlaceTrackeable; }
    public void setEnlaceTrackeable(String enlaceTrackeable) { this.enlaceTrackeable = enlaceTrackeable; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public Integer getClicsTotal() { return clicsTotal; }
    public void setClicsTotal(Integer clicsTotal) { this.clicsTotal = clicsTotal; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
