package com.remington.unieats.marketplace.model.repository;

import com.remington.unieats.marketplace.model.entity.EmailTemplate;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface EmailTemplateRepository extends JpaRepository<EmailTemplate, Long> {
    Optional<EmailTemplate> findByNombre(String nombre);
    java.util.List<EmailTemplate> findByCategoria(String categoria);
}
