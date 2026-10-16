package com.foodrush.api.dto.request;

import jakarta.validation.constraints.NotBlank;

public record LoginRequestDTO(
  @NotBlank(message = "O refresh token é obrigatório") String email,
  @NotBlank(message = "O refresh token é obrigatório") String password
) {}
