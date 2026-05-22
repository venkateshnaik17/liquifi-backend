package com.liquifi.model.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;

@Data
@Builder
public class CibilResultDto {
    private String sessionId;
    private String fullName;
    private Integer creditScore;
    private String scoreBand;
    private LocalDate scoreAsOfDate;
    private Map<String, Object> creditSummary;
    private List<Map<String, Object>> loanEligibility;
    private OffsetDateTime extractedAt;
}
