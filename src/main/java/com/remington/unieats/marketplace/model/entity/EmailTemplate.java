package com.remington.unieats.marketplace.model.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "email_templates")
public class EmailTemplate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 100)
    private String nombre;

    @Column(columnDefinition = "TEXT")
    private String descripcion;

    @Column(name = "asunto_template", length = 200)
    private String asuntoTemplate;

    @Column(name = "contenido_html", nullable = false, columnDefinition = "TEXT")
    private String contenidoHtml;

    @Column(name = "variables_disponibles", columnDefinition = "TEXT")
    private String variablesDisponibles;

    @Column(length = 50)
    private String categoria;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at")
    private LocalDateTime updatedAt = LocalDateTime.now();

    public EmailTemplate() {}

    public EmailTemplate(String nombre, String contenidoHtml) {
        this.nombre = nombre;
        this.contenidoHtml = contenidoHtml;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public String getAsuntoTemplate() { return asuntoTemplate; }
    public void setAsuntoTemplate(String asuntoTemplate) { this.asuntoTemplate = asuntoTemplate; }

    public String getContenidoHtml() { return contenidoHtml; }
    public void setContenidoHtml(String contenidoHtml) { this.contenidoHtml = contenidoHtml; }

    public String getVariablesDisponibles() { return variablesDisponibles; }
    public void setVariablesDisponibles(String variablesDisponibles) { this.variablesDisponibles = variablesDisponibles; }

    public String getCategoria() { return categoria; }
    public void setCategoria(String categoria) { this.categoria = categoria; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
