package com.remington.unieats.marketplace.model.repository;

import com.remington.unieats.marketplace.model.entity.CampaignLink;
import com.remington.unieats.marketplace.model.entity.MarketingCampaign;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface CampaignLinkRepository extends JpaRepository<CampaignLink, Long> {
    List<CampaignLink> findByCampaign(MarketingCampaign campaign);
    Optional<CampaignLink> findByEnlaceTrackeable(String enlaceTrackeable);
}
