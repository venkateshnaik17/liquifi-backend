package com.liquifi.service;

import com.liquifi.exception.SessionNotFoundException;
import com.liquifi.model.dto.CibilResultDto;
import com.liquifi.model.entity.CibilResult;
import com.liquifi.repository.CibilResultRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ResultStoreService {

    private final CibilResultRepository cibilResultRepository;

    @Transactional(readOnly = true)
    public CibilResultDto getResult(String sessionId) {
        CibilResult result = cibilResultRepository.findBySessionId(sessionId)
            .orElseThrow(() -> new SessionNotFoundException(sessionId));

        return CibilResultDto.builder()
            .sessionId(result.getSessionId())
            .fullName(result.getFullName())
            .creditScore(result.getCreditScore())
            .scoreBand(result.getScoreBand())
            .scoreAsOfDate(result.getScoreAsOfDate())
            .creditSummary(result.getCreditSummary())
            .loanEligibility(result.getLoanEligibility())
            .extractedAt(result.getExtractedAt())
            .build();
    }

    @Transactional(readOnly = true)
    public boolean resultExists(String sessionId) {
        return cibilResultRepository.existsBySessionId(sessionId);
    }
}
