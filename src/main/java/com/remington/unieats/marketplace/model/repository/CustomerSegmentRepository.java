package com.remington.unieats.marketplace.model.repository;

import com.remington.unieats.marketplace.model.entity.CustomerSegment;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface CustomerSegmentRepository extends JpaRepository<CustomerSegment, Long> {
    Optional<CustomerSegment> findByNombre(String nombre);
}
