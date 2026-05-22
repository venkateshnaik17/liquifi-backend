package com.liquifi.model.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDate;

@Data
public class CibilRequestDto {

    @NotBlank(message = "Full name is required")
    @Size(min = 2, max = 255, message = "Full name must be between 2 and 255 characters")
    @Pattern(regexp = "^[a-zA-Z\\s.'-]+$", message = "Full name contains invalid characters")
    private String fullName;

    @NotNull(message = "Date of birth is required")
    @Past(message = "Date of birth must be in the past")
    private LocalDate dob;

    @NotBlank(message = "Mobile number is required")
    @Pattern(regexp = "^[6-9]\\d{9}$", message = "Invalid Indian mobile number")
    private String mobileNumber;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email address")
    @Size(max = 255)
    private String email;

    @NotBlank(message = "PAN card is required")
    @Pattern(regexp = "^[A-Z]{5}[0-9]{4}[A-Z]$", message = "Invalid PAN card format")
    private String panCard;

    @AssertTrue(message = "Consent is required to proceed")
    private Boolean consentGiven;
}
