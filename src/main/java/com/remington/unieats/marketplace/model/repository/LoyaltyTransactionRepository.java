package com.remington.unieats.marketplace.model.repository;

import com.remington.unieats.marketplace.model.entity.LoyaltyTransaction;
import com.remington.unieats.marketplace.model.entity.LoyaltyPoints;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface LoyaltyTransactionRepository extends JpaRepository<LoyaltyTransaction, Long> {
    List<LoyaltyTransaction> findByLoyaltyPoints(LoyaltyPoints loyaltyPoints);
    List<LoyaltyTransaction> findByTipoTransaccion(String tipoTransaccion);
}
