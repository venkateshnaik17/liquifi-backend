package com.liquifi.service;

import com.liquifi.model.dto.CibilRequestDto;
import com.liquifi.model.entity.ConsentLog;
import com.liquifi.repository.ConsentLogRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ConsentService {

    private static final String CONSENT_TEXT =
        "I agree to the Terms and Conditions of TUCIBIL and hereby provide explicit consent " +
        "to share my Credit Information with Urban Money Private Limited.";

    private final ConsentLogRepository consentLogRepository;

    @Transactional
    public void recordConsent(String sessionId, CibilRequestDto dto, String ipAddress, String userAgent) {
        ConsentLog log = ConsentLog.builder()
            .sessionId(sessionId)
            .fullName(dto.getFullName())
            .dob(dto.getDob())
            .mobileNumber(dto.getMobileNumber())
            .email(dto.getEmail())
            .panCard(dto.getPanCard())
            .consentText(CONSENT_TEXT)
            .consentGiven(dto.getConsentGiven())
            .ipAddress(ipAddress)
            .userAgent(userAgent)
            .build();

        consentLogRepository.save(log);
        ConsentService.log.info("Consent recorded for sessionId={}", sessionId);
    }

    public String getConsentText() {
        return CONSENT_TEXT;
    }
}
