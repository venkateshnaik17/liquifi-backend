package com.liquifi.repository;

import com.liquifi.model.entity.CibilSession;
import com.liquifi.model.entity.CibilSession.SessionStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface SessionRepository extends JpaRepository<CibilSession, UUID> {
    Optional<CibilSession> findBySessionId(String sessionId);
    List<CibilSession> findAllByStatus(SessionStatus status);

    @Modifying
    @Query("UPDATE CibilSession s SET s.status = 'EXPIRED' WHERE s.expiresAt < :now AND s.status NOT IN ('COMPLETED','FAILED','EXPIRED')")
    int expireStaleSessionsBefore(OffsetDateTime now);
}
