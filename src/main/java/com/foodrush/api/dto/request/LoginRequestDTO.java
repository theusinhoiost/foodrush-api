package com.foodrush.api.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Credentials payload for user authentication")
public record LoginRequestDTO(
  @Schema(
    description = "Registered user email address",
    example = "cliente@foodrush.com"
  )
  @NotBlank(message = "O e-mail é obrigatório")
  @Email(message = "Formato de e-mail inválido")
  String email,

  @Schema(description = "Plaintext user password", example = "123456")
  @NotBlank(message = "A senha é obrigatória")
  String password
) {}
