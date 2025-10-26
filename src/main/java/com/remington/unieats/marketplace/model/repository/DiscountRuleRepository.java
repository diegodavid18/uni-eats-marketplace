package com.remington.unieats.marketplace.model.repository;

import com.remington.unieats.marketplace.model.entity.DiscountRule;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface DiscountRuleRepository extends JpaRepository<DiscountRule, Long> {
    List<DiscountRule> findByActiva(Boolean activa);
    List<DiscountRule> findByTipoRegla(String tipoRegla);
}
