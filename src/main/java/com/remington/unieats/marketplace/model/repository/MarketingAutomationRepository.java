package com.remington.unieats.marketplace.model.repository;

import com.remington.unieats.marketplace.model.entity.MarketingAutomation;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface MarketingAutomationRepository extends JpaRepository<MarketingAutomation, Long> {
    List<MarketingAutomation> findByActiva(Boolean activa);
    List<MarketingAutomation> findByTipoTrigger(String tipoTrigger);
}
