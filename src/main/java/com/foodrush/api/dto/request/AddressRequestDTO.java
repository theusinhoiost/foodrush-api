package com.foodrush.api.dto.request;

import com.foodrush.api.validation.ValidCEP;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(
  description = "Address input payload requiring only the Brazilian postal code (CEP), street number, and optional complement. Street details and coordinates are resolved automatically."
)
public record AddressRequestDTO(
  @Schema(
    description = "Brazilian postal code (CEP) with 8 digits (hyphen optional)",
    example = "01001-000"
  )
  @NotBlank(message = "O CEP é obrigatório")
  @ValidCEP(message = "O CEP informado é inválido (use 8 dígitos)")
  String cep,

  @Schema(description = "Building or street number", example = "100")
  @NotBlank(message = "O número do endereço é obrigatório")
  @Size(max = 20, message = "O número deve ter no máximo 20 caracteres")
  String number,

  @Schema(
    description = "Optional address complement (apartment, suite, store number)",
    example = "Store 1"
  )
  @Size(max = 100, message = "O complemento deve ter no máximo 100 caracteres")
  String complement
) {}

