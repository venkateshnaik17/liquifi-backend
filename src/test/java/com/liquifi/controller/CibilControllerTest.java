package com.liquifi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.liquifi.model.dto.CibilRequestDto;
import com.liquifi.service.CibilService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CibilController.class)
class CibilControllerTest {

    @Autowired MockMvc mockMvc;
    @Autowired ObjectMapper objectMapper;
    @MockBean  CibilService cibilService;

    @Test
    void initiate_validRequest_returns200WithSessionId() throws Exception {
        CibilRequestDto dto = new CibilRequestDto();
        dto.setFullName("Ravi Kumar");
        dto.setDob(LocalDate.of(1990, 5, 15));
        dto.setMobileNumber("9876543210");
        dto.setEmail("ravi@example.com");
        dto.setPanCard("ABCDE1234F");
        dto.setConsentGiven(true);

        Mockito.when(cibilService.initiateCibilFlow(Mockito.any(), Mockito.any(), Mockito.any()))
               .thenReturn("test-session-001");

        mockMvc.perform(post("/cibil/initiate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.sessionId").value("test-session-001"));
    }

    @Test
    void initiate_missingConsent_returns400() throws Exception {
        CibilRequestDto dto = new CibilRequestDto();
        dto.setFullName("Ravi Kumar");
        dto.setDob(LocalDate.of(1990, 5, 15));
        dto.setMobileNumber("9876543210");
        dto.setEmail("ravi@example.com");
        dto.setPanCard("ABCDE1234F");
        dto.setConsentGiven(false); // consent not given

        mockMvc.perform(post("/cibil/initiate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
            .andExpect(status().isBadRequest());
    }

    @Test
    void initiate_invalidPan_returns400() throws Exception {
        CibilRequestDto dto = new CibilRequestDto();
        dto.setFullName("Ravi Kumar");
        dto.setDob(LocalDate.of(1990, 5, 15));
        dto.setMobileNumber("9876543210");
        dto.setEmail("ravi@example.com");
        dto.setPanCard("INVALID");
        dto.setConsentGiven(true);

        mockMvc.perform(post("/cibil/initiate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.fieldErrors.panCard").exists());
    }
}
