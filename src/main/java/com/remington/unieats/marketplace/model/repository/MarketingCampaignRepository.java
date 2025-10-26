package com.remington.unieats.marketplace.model.repository;

import com.remington.unieats.marketplace.model.entity.MarketingCampaign;
import com.remington.unieats.marketplace.model.entity.CustomerSegment;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface MarketingCampaignRepository extends JpaRepository<MarketingCampaign, Long> {
    List<MarketingCampaign> findByEstado(String estado);
    List<MarketingCampaign> findBySegmento(CustomerSegment segmento);
    List<MarketingCampaign> findByTipo(String tipo);
}
