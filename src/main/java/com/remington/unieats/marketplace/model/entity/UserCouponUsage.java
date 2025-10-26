package com.remington.unieats.marketplace.model.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "user_coupon_usage")
public class UserCouponUsage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "coupon_id", nullable = false)
    private Coupon coupon;

    @Column(name = "pedido_id")
    private Long pedidoId;

    @Column(name = "monto_descuento", precision = 10, scale = 2)
    private BigDecimal montoDescuento;

    @Column(name = "usado_en")
    private LocalDateTime usadoEn = LocalDateTime.now();

    public UserCouponUsage() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }

    public Coupon getCoupon() { return coupon; }
    public void setCoupon(Coupon coupon) { this.coupon = coupon; }

    public Long getPedidoId() { return pedidoId; }
    public void setPedidoId(Long pedidoId) { this.pedidoId = pedidoId; }

    public BigDecimal getMontoDescuento() { return montoDescuento; }
    public void setMontoDescuento(BigDecimal montoDescuento) { this.montoDescuento = montoDescuento; }

    public LocalDateTime getUsadoEn() { return usadoEn; }
    public void setUsadoEn(LocalDateTime usadoEn) { this.usadoEn = usadoEn; }
}
