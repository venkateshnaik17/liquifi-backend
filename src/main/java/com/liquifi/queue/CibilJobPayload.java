package com.liquifi.queue;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CibilJobPayload {
    private String sessionId;
    private String fullName;
    private LocalDate dob;
    private String mobileNumber;
    private String email;
    private String panCard;
    private long enqueuedAt;
}
