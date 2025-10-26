package com.remington.unieats.marketplace.model.repository;

import com.remington.unieats.marketplace.model.entity.BehaviorLog;
import com.remington.unieats.marketplace.model.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDateTime;
import java.util.List;

public interface BehaviorLogRepository extends JpaRepository<BehaviorLog, Long> {
    List<BehaviorLog> findByUsuarioOrderByCreatedAtDesc(Usuario usuario);
    List<BehaviorLog> findByUsuarioAndTipoEvento(Usuario usuario, String tipoEvento);
    List<BehaviorLog> findByCreatedAtAfter(LocalDateTime fecha);
}
