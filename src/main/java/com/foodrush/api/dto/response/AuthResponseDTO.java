package com.foodrush.api.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Authentication response containing JWT access and refresh tokens")
public record AuthResponseDTO(
  @Schema(
    description = "Short-lived JWT access token (15 minutes)",
    example = "eyJhbGciOiJIUzUxMiJ9..."
  )
  String accessToken,

  @Schema(
    description = "Long-lived JWT refresh token (7 days)",
    example = "eyJhbGciOiJIUzUxMiJ9..."
  )
  String refreshToken,

  @Schema(
    description = "HTTP Authorization header scheme",
    example = "Bearer"
  )
  String tokenType
) {
  public AuthResponseDTO(String accessToken, String refreshToken) {
    this(accessToken, refreshToken, "Bearer");
  }
}