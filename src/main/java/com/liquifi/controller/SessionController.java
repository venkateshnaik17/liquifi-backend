package com.liquifi.controller;

import com.liquifi.model.dto.SessionStatusDto;
import com.liquifi.service.CibilService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/session")
@RequiredArgsConstructor
public class SessionController {

    private final CibilService cibilService;

    /**
     * GET /api/session/status/{sessionId}
     * Frontend polls this to track session progress.
     */
    @GetMapping("/status/{sessionId}")
    public ResponseEntity<SessionStatusDto> getStatus(@PathVariable String sessionId) {
        return ResponseEntity.ok(cibilService.getSessionStatus(sessionId));
    }
}
