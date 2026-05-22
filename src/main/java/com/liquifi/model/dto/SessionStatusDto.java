package com.liquifi.model.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SessionStatusDto {
    private String sessionId;
    private String status;
    private String errorMessage;
    private Boolean resultReady;
}
