package com.bridgelabz.authservice.dto;

import jakarta.validation.constraints.NotBlank;

public class TokenRefreshRequestDTO {
    @NotBlank(message = "Refresh token must not be blank")
    private String refreshToken;

    public TokenRefreshRequestDTO() {}

    public TokenRefreshRequestDTO(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public String getRefreshToken() 
    {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}
