package com.foodrush.api.dto.response;

public record AuthResponseDTO(
        String accessToken,
        String refreshToken,
        String tokenType
) {
    public AuthResponseDTO(String accessToken, String refreshToken) {
        this(accessToken, refreshToken, "Bearer");
    }
}