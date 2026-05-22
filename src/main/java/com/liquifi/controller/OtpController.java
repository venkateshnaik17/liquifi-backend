package com.liquifi.controller;

import com.liquifi.model.dto.OtpSubmitDto;
import com.liquifi.model.entity.CibilSession.SessionStatus;
import com.liquifi.service.OtpRelayService;
import com.liquifi.service.SessionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/otp")
@RequiredArgsConstructor
@Slf4j
public class OtpController {

    private final OtpRelayService otpRelayService;
    private final SessionService sessionService;

    /**
     * POST /api/otp/submit
     * User submits OTP → backend stores in Redis → Selenium worker picks it up.
     * OTP is NEVER persisted to DB.
     */
    @PostMapping("/submit")
    public ResponseEntity<Map<String, String>> submitOtp(@Valid @RequestBody OtpSubmitDto dto) {
        // Validate session exists and is in OTP_AWAITED state
        var session = sessionService.getSession(dto.getSessionId());

        if (session.getStatus() != SessionStatus.OTP_AWAITED) {
            return ResponseEntity.badRequest().body(Map.of(
                "message", "Session is not waiting for OTP. Current status: " + session.getStatus()
            ));
        }

        otpRelayService.relayOtp(dto.getSessionId(), dto.getOtp());
        sessionService.updateStatus(dto.getSessionId(), SessionStatus.OTP_SUBMITTED);

        log.info("OTP submitted for session={}", dto.getSessionId());
        return ResponseEntity.ok(Map.of("message", "OTP submitted successfully."));
    }
}
