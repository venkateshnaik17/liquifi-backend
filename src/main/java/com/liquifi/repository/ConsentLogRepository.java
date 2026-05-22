package com.liquifi.repository;

import com.liquifi.model.entity.ConsentLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ConsentLogRepository extends JpaRepository<ConsentLog, UUID> {
    Optional<ConsentLog> findBySessionId(String sessionId);
    List<ConsentLog> findAllByMobileNumber(String mobileNumber);
    boolean existsBySessionId(String sessionId);
}
