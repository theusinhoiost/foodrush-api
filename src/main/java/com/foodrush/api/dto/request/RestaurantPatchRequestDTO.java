package com.foodrush.api.dto.request;

import com.foodrush.api.validation.ValidCNPJ;
import com.foodrush.api.validation.ValidPhone;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.UUID;

@Schema(
  description = "Request payload for partially updating a restaurant (all fields are optional)"
)
public record RestaurantPatchRequestDTO(
  @Schema(
    description = "UUID of the new restaurant owner",
    example = "22222222-2222-2222-2222-222222222222"
  )
  UUID ownerId,

  @Schema(
    description = "Updated commercial trade name",
    example = "FoodRush Burger Express"
  )
  @Size(
    max = 120,
    message = "O nome fantasia deve ter no máximo 120 caracteres"
  )
  String tradeName,

  @Schema(description = "Updated 14-digit CNPJ", example = "48555407000133")
  @ValidCNPJ(message = "O CNPJ informado é inválido")
  String cnpj,

  @Schema(
    description = "Updated Brazilian mobile phone number",
    example = "11988881111"
  )
  @ValidPhone(message = "O número de celular informado é inválido")
  String phone,

  @Schema(description = "Updated base delivery fee in BRL", example = "7.50")
  @DecimalMin(
    value = "0.00",
    message = "A taxa de entrega não pode ser negativa"
  )
  BigDecimal baseDeliveryFee,

  @Schema(description = "Updated restaurant postal address")
  @Valid
  AddressRequestDTO address,

  @Schema(
    description = "Operational status (true = open, false = closed)",
    example = "true"
  )
  Boolean isOpen
) {}
