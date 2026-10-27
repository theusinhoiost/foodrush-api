package com.foodrush.api.dto.request;

import com.foodrush.api.validation.ValidPhone;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

@Schema(
  description = "Request payload for partially updating user profile information"
)
public record UserPatchRequestDTO(
  @Schema(description = "Updated full name", example = "Matheus Silva")
  @Size(max = 100, message = "O nome deve ter no máximo 100 caracteres")
  String name,

  @Schema(
    description = "Updated email address",
    example = "matheus.novo@foodrush.com"
  )
  @Email(message = "Formato de e-mail inválido")
  @Size(max = 100, message = "O e-mail deve ter no máximo 100 caracteres")
  String email,

  @Schema(
    description = "Updated Brazilian mobile phone number",
    example = "11991112222"
  )
  @ValidPhone(message = "O número de celular informado é inválido")
  String phone,

  @Schema(description = "Updated postal address")
  @Valid
  AddressRequestDTO address
) {}
