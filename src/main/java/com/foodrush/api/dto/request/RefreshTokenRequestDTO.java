package com.foodrush.api.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Payload for requesting a new access token using a refresh token")
public record RefreshTokenRequestDTO(
  @Schema(
    description = "Valid JWT refresh token issued during login",
    example = "eyJhbGciOiJIUzUxMiJ9..."
  )
  @NotBlank(message = "O refresh token é obrigatório")
  String refreshToken
) {}
