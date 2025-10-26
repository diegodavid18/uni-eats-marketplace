package com.remington.unieats.marketplace.model.repository;

import com.remington.unieats.marketplace.model.entity.UserCouponUsage;
import com.remington.unieats.marketplace.model.entity.Usuario;
import com.remington.unieats.marketplace.model.entity.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface UserCouponUsageRepository extends JpaRepository<UserCouponUsage, Long> {
    List<UserCouponUsage> findByUsuario(Usuario usuario);
    List<UserCouponUsage> findByCoupon(Coupon coupon);
}
