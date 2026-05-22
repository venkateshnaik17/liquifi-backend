package com.liquifi.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class OtpSubmitDto {

    @NotBlank(message = "Session ID is required")
    private String sessionId;

    @NotBlank(message = "OTP is required")
    @Pattern(regexp = "^\\d{4,8}$", message = "OTP must be 4 to 8 digits")
    private String otp;
}
