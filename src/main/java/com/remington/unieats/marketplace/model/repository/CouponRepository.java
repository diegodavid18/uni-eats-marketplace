package com.remington.unieats.marketplace.model.repository;

import com.remington.unieats.marketplace.model.entity.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface CouponRepository extends JpaRepository<Coupon, Long> {
    Optional<Coupon> findByCodigo(String codigo);
    List<Coupon> findByActivo(Boolean activo);
}
