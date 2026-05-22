package com.liquifi.controller;

import com.liquifi.model.dto.CibilRequestDto;
import com.liquifi.model.dto.CibilResultDto;
import com.liquifi.service.CibilService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/cibil")
@RequiredArgsConstructor
@Slf4j
public class CibilController {

    private final CibilService cibilService;

    /**
     * POST /api/cibil/initiate
     * Accepts user form data + consent, creates session, enqueues automation job.
     */
    @PostMapping("/initiate")
    public ResponseEntity<Map<String, String>> initiate(
            @Valid @RequestBody CibilRequestDto dto,
            HttpServletRequest request) {

        String ipAddress = extractClientIp(request);
        String userAgent = request.getHeader("User-Agent");
        String sessionId = cibilService.initiateCibilFlow(dto, ipAddress, userAgent);

        return ResponseEntity.ok(Map.of(
            "sessionId", sessionId,
            "message", "CIBIL check initiated. Please wait for OTP."
        ));
    }

    /**
     * GET /api/cibil/result/{sessionId}
     * Returns extracted CIBIL result once automation is complete.
     */
    @GetMapping("/result/{sessionId}")
    public ResponseEntity<CibilResultDto> getResult(@PathVariable String sessionId) {
        return ResponseEntity.ok(cibilService.getResult(sessionId));
    }

    private String extractClientIp(HttpServletRequest request) {
        String xForwardedFor = request.getHeader("X-Forwarded-For");
        if (xForwardedFor != null && !xForwardedFor.isEmpty()) {
            return xForwardedFor.split(",")[0].trim();
        }
        return request.getRemoteAddr();
    }
}
