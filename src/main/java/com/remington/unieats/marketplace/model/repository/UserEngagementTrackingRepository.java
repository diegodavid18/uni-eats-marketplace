package com.remington.unieats.marketplace.model.repository;

import com.remington.unieats.marketplace.model.entity.UserEngagementTracking;
import com.remington.unieats.marketplace.model.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserEngagementTrackingRepository extends JpaRepository<UserEngagementTracking, Long> {
    Optional<UserEngagementTracking> findByUsuario(Usuario usuario);
}
