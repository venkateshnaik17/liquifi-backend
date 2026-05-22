package com.liquifi.service;

import com.liquifi.model.dto.CibilRequestDto;
import com.liquifi.queue.CibilJobPublisher;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CibilServiceTest {

    @Mock ConsentService consentService;
    @Mock SessionService sessionService;
    @Mock CibilJobPublisher jobPublisher;
    @Mock ResultStoreService resultStoreService;

    @InjectMocks CibilService cibilService;

    @Test
    void initiateCibilFlow_createsSessionAndPublishesJob() {
        when(sessionService.createSession(any(), any(), any(), any(), any()))
            .thenReturn("session-abc");

        CibilRequestDto dto = new CibilRequestDto();
        dto.setFullName("Ravi Kumar");
        dto.setDob(LocalDate.of(1990, 5, 15));
        dto.setMobileNumber("9876543210");
        dto.setEmail("ravi@example.com");
        dto.setPanCard("ABCDE1234F");
        dto.setConsentGiven(true);

        String sessionId = cibilService.initiateCibilFlow(dto, "127.0.0.1", "TestAgent");

        assertThat(sessionId).isEqualTo("session-abc");
        verify(consentService, times(1)).recordConsent(eq("session-abc"), any(), any(), any());
        verify(jobPublisher, times(1)).publish(any());
    }
}
