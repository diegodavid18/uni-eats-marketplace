package com.remington.unieats.marketplace.model.repository;

import com.remington.unieats.marketplace.model.entity.CustomerProfile;
import com.remington.unieats.marketplace.model.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;
import java.util.Optional;

public interface CustomerProfileRepository extends JpaRepository<CustomerProfile, Long> {
    Optional<CustomerProfile> findByUsuario(Usuario usuario);
    List<CustomerProfile> findBySegment(String segment);
    
    @Query("SELECT cp FROM CustomerProfile cp ORDER BY cp.lifetimeValue DESC")
    List<CustomerProfile> findTopCustomersByLifetimeValue();
}
