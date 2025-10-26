package com.remington.unieats.marketplace.model.repository;

import com.remington.unieats.marketplace.model.entity.CampaignSend;
import com.remington.unieats.marketplace.model.entity.MarketingCampaign;
import com.remington.unieats.marketplace.model.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CampaignSendRepository extends JpaRepository<CampaignSend, Long> {
    List<CampaignSend> findByCampaign(MarketingCampaign campaign);
    List<CampaignSend> findByUsuario(Usuario usuario);
    List<CampaignSend> findByEstado(String estado);
    List<CampaignSend> findByCampaignAndEstado(MarketingCampaign campaign, String estado);
}
