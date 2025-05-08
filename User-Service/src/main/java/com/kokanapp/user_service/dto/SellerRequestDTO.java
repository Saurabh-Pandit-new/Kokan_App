package com.kokanapp.user_service.dto;

import jakarta.validation.constraints.NotBlank;

public class SellerRequestDTO {

    @NotBlank
    private String email;

    @NotBlank
    private String reason;  // Optional: reason for applying as a seller

    // No-argument constructor
    public SellerRequestDTO() {}

    // All-arguments constructor
    public SellerRequestDTO(String email, String reason) {
        this.email = email;
        this.reason = reason;
    }

    // Getter and Setter for email
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    // Getter and Setter for reason
    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
