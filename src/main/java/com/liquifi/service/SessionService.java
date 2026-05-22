package com.liquifi.service;

import com.liquifi.exception.SessionNotFoundException;
import com.liquifi.model.dto.SessionStatusDto;
import com.liquifi.model.entity.CibilSession;
import com.liquifi.model.entity.CibilSession.SessionStatus;
import com.liquifi.repository.SessionRepository;
import com.liquifi.util.SessionIdGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.OffsetDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class SessionService {

    private final SessionRepository sessionRepository;
    private final SessionIdGenerator sessionIdGenerator;

    @Value("${liquifi.session.ttl-minutes:30}")
    private int sessionTtlMinutes;

    @Transactional
    public String createSession(String fullName, LocalDate dob, String mobileNumber,
                                String email, String panCard) {
        String sessionId = sessionIdGenerator.generate();

        CibilSession session = CibilSession.builder()
            .sessionId(sessionId)
            .fullName(fullName)
            .dob(dob)
            .mobileNumber(mobileNumber)
            .email(email)
            .panCard(panCard)
            .status(SessionStatus.PENDING)
            .retryCount(0)
            .expiresAt(OffsetDateTime.now().plusMinutes(sessionTtlMinutes))
            .build();

        sessionRepository.save(session);
        log.info("Created session={} for mobile={}", sessionId, mobileNumber.substring(0, 4) + "XXXXXX");
        return sessionId;
    }

    @Transactional
    public void updateStatus(String sessionId, SessionStatus status) {
        CibilSession session = getSession(sessionId);
        session.setStatus(status);
        sessionRepository.save(session);
        log.info("Session={} status updated to={}", sessionId, status);
    }

    @Transactional
    public void markFailed(String sessionId, String errorMessage) {
        CibilSession session = getSession(sessionId);
        session.setStatus(SessionStatus.FAILED);
        session.setErrorMessage(errorMessage);
        sessionRepository.save(session);
        log.warn("Session={} marked FAILED. reason={}", sessionId, errorMessage);
    }

    @Transactional(readOnly = true)
    public SessionStatusDto getStatus(String sessionId) {
        CibilSession session = getSession(sessionId);
        return SessionStatusDto.builder()
            .sessionId(sessionId)
            .status(session.getStatus().name())
            .errorMessage(session.getErrorMessage())
            .resultReady(session.getStatus() == SessionStatus.COMPLETED)
            .build();
    }

    @Transactional(readOnly = true)
    public CibilSession getSession(String sessionId) {
        return sessionRepository.findBySessionId(sessionId)
            .orElseThrow(() -> new SessionNotFoundException(sessionId));
    }

    @Scheduled(fixedDelay = 300_000) // every 5 minutes
    @Transactional
    public void expireStaleSessions() {
        int count = sessionRepository.expireStaleSessionsBefore(OffsetDateTime.now());
        if (count > 0) {
            log.info("Expired {} stale sessions", count);
        }
    }
}
