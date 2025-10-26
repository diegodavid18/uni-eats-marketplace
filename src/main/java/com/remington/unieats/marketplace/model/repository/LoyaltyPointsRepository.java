package com.remington.unieats.marketplace.model.repository;

import com.remington.unieats.marketplace.model.entity.LoyaltyPoints;
import com.remington.unieats.marketplace.model.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface LoyaltyPointsRepository extends JpaRepository<LoyaltyPoints, Long> {
    Optional<LoyaltyPoints> findByUsuario(Usuario usuario);
}
