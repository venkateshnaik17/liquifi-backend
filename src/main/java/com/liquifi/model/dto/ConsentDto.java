package com.liquifi.model.dto;

import lombok.Builder;
import lombok.Data;

import java.time.OffsetDateTime;

@Data
@Builder
public class ConsentDto {
    private String sessionId;
    private String consentText;
    private Boolean consentGiven;
    private OffsetDateTime consentedAt;
}
