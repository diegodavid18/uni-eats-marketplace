package com.remington.unieats.marketplace.model.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "campaign_sends", indexes = {
    @Index(name = "idx_campaign_sends_estado", columnList = "estado"),
    @Index(name = "idx_campaign_sends_usuario", columnList = "usuario_id")
})
public class CampaignSend {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "campaign_id", nullable = false)
    private MarketingCampaign campaign;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "email_template_id")
    private EmailTemplate emailTemplate;

    @Column(length = 200)
    private String asunto;

    @Column(name = "contenido_enviado", columnDefinition = "TEXT")
    private String contenidoEnviado;

    @Column(length = 50)
    private String estado;

    @Column(name = "email_address", length = 255)
    private String emailAddress;

    @Column(name = "fecha_envio")
    private LocalDateTime fechaEnvio;

    @Column(name = "fecha_apertura")
    private LocalDateTime fechaApertura;

    @Column(name = "fecha_clic")
    private LocalDateTime fechaClic;

    @Column(name = "clic_link_ids", columnDefinition = "TEXT")
    private String clicLinkIds;

    @Column(name = "reason_failed", length = 500)
    private String reasonFailed;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    public CampaignSend() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public MarketingCampaign getCampaign() { return campaign; }
    public void setCampaign(MarketingCampaign campaign) { this.campaign = campaign; }

    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }

    public EmailTemplate getEmailTemplate() { return emailTemplate; }
    public void setEmailTemplate(EmailTemplate emailTemplate) { this.emailTemplate = emailTemplate; }

    public String getAsunto() { return asunto; }
    public void setAsunto(String asunto) { this.asunto = asunto; }

    public String getContenidoEnviado() { return contenidoEnviado; }
    public void setContenidoEnviado(String contenidoEnviado) { this.contenidoEnviado = contenidoEnviado; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public String getEmailAddress() { return emailAddress; }
    public void setEmailAddress(String emailAddress) { this.emailAddress = emailAddress; }

    public LocalDateTime getFechaEnvio() { return fechaEnvio; }
    public void setFechaEnvio(LocalDateTime fechaEnvio) { this.fechaEnvio = fechaEnvio; }

    public LocalDateTime getFechaApertura() { return fechaApertura; }
    public void setFechaApertura(LocalDateTime fechaApertura) { this.fechaApertura = fechaApertura; }

    public LocalDateTime getFechaClic() { return fechaClic; }
    public void setFechaClic(LocalDateTime fechaClic) { this.fechaClic = fechaClic; }

    public String getClicLinkIds() { return clicLinkIds; }
    public void setClicLinkIds(String clicLinkIds) { this.clicLinkIds = clicLinkIds; }

    public String getReasonFailed() { return reasonFailed; }
    public void setReasonFailed(String reasonFailed) { this.reasonFailed = reasonFailed; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
