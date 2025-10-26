package com.remington.unieats.marketplace.model.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "customer_profiles")
public class CustomerProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "usuario_id", nullable = false, unique = true)
    private Usuario usuario;

    @Column(length = 50)
    private String segment;

    @Column(name = "total_purchases")
    private Integer totalPurchases = 0;

    @Column(name = "total_spent", precision = 10, scale = 2)
    private BigDecimal totalSpent = BigDecimal.ZERO;

    @Column(name = "average_order_value", precision = 10, scale = 2)
    private BigDecimal averageOrderValue;

    @Column(name = "last_purchase_date")
    private LocalDateTime lastPurchaseDate;

    @Column(name = "lifetime_value", precision = 15, scale = 2)
    private BigDecimal lifetimeValue = BigDecimal.ZERO;

    @Column(name = "purchase_frequency")
    private Integer purchaseFrequency;

    @Column(name = "preferred_categories", columnDefinition = "TEXT")
    private String preferredCategories;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at")
    private LocalDateTime updatedAt = LocalDateTime.now();

    public CustomerProfile() {}

    public CustomerProfile(Usuario usuario) {
        this.usuario = usuario;
        this.segment = "NEW";
    }

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }

    public String getSegment() { return segment; }
    public void setSegment(String segment) { this.segment = segment; }

    public Integer getTotalPurchases() { return totalPurchases; }
    public void setTotalPurchases(Integer totalPurchases) { this.totalPurchases = totalPurchases; }

    public BigDecimal getTotalSpent() { return totalSpent; }
    public void setTotalSpent(BigDecimal totalSpent) { this.totalSpent = totalSpent; }

    public BigDecimal getAverageOrderValue() { return averageOrderValue; }
    public void setAverageOrderValue(BigDecimal averageOrderValue) { this.averageOrderValue = averageOrderValue; }

    public LocalDateTime getLastPurchaseDate() { return lastPurchaseDate; }
    public void setLastPurchaseDate(LocalDateTime lastPurchaseDate) { this.lastPurchaseDate = lastPurchaseDate; }

    public BigDecimal getLifetimeValue() { return lifetimeValue; }
    public void setLifetimeValue(BigDecimal lifetimeValue) { this.lifetimeValue = lifetimeValue; }

    public Integer getPurchaseFrequency() { return purchaseFrequency; }
    public void setPurchaseFrequency(Integer purchaseFrequency) { this.purchaseFrequency = purchaseFrequency; }

    public String getPreferredCategories() { return preferredCategories; }
    public void setPreferredCategories(String preferredCategories) { this.preferredCategories = preferredCategories; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
