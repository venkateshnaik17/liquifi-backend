package com.liquifi.service;

import com.liquifi.model.dto.CibilRequestDto;
import com.liquifi.model.dto.CibilResultDto;
import com.liquifi.model.dto.SessionStatusDto;
import com.liquifi.model.entity.CibilSession.SessionStatus;
import com.liquifi.queue.CibilJobPayload;
import com.liquifi.queue.CibilJobPublisher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class CibilService {

    private final ConsentService consentService;
    private final SessionService sessionService;
    private final CibilJobPublisher jobPublisher;
    private final ResultStoreService resultStoreService;

    /**
     * Entry point: validates consent, creates session, records audit, enqueues Selenium job.
     */
    @Transactional
    public String initiateCibilFlow(CibilRequestDto dto, String ipAddress, String userAgent) {
        // 1. Create session
        String sessionId = sessionService.createSession(
            dto.getFullName(), dto.getDob(),
            dto.getMobileNumber(), dto.getEmail(), dto.getPanCard()
        );

        // 2. Record consent log (immutable audit)
        consentService.recordConsent(sessionId, dto, ipAddress, userAgent);

        // 3. Publish job to Redis → Selenium worker picks it up
        CibilJobPayload payload = CibilJobPayload.builder()
            .sessionId(sessionId)
            .fullName(dto.getFullName())
            .dob(dto.getDob())
            .mobileNumber(dto.getMobileNumber())
            .email(dto.getEmail())
            .panCard(dto.getPanCard())
            .enqueuedAt(System.currentTimeMillis())
            .build();

        jobPublisher.publish(payload);
        log.info("CIBIL flow initiated. sessionId={}", sessionId);
        return sessionId;
    }

    public SessionStatusDto getSessionStatus(String sessionId) {
        return sessionService.getStatus(sessionId);
    }

    public CibilResultDto getResult(String sessionId) {
        return resultStoreService.getResult(sessionId);
    }
}
