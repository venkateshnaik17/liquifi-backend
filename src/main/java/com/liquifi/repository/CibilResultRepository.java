package com.liquifi.repository;

import com.liquifi.model.entity.CibilResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface CibilResultRepository extends JpaRepository<CibilResult, UUID> {
    Optional<CibilResult> findBySessionId(String sessionId);
    Optional<CibilResult> findTopByMobileNumberOrderByCreatedAtDesc(String mobileNumber);
    boolean existsBySessionId(String sessionId);
}
