package com.foodrush.api.dto.request;

import com.foodrush.api.validation.ValidCNPJ;
import com.foodrush.api.validation.ValidPhone;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.UUID;

@Schema(description = "Request payload for creating or updating a restaurant")
public record RestaurantRequestDTO(
  @Schema(
    description = "UUID of the user who owns the restaurant",
    example = "22222222-2222-2222-2222-222222222222"
  )
  @NotNull(message = "O ID do proprietário é obrigatório")
  UUID ownerId,

  @Schema(
    description = "Commercial trade name of the restaurant",
    example = "FoodRush Burger Artesanal"
  )
  @NotBlank(message = "O nome fantasia é obrigatório")
  @Size(
    max = 120,
    message = "O nome fantasia deve ter no máximo 120 caracteres"
  )
  String tradeName,

  @Schema(
    description = "Valid 14-digit Brazilian company tax ID (CNPJ)",
    example = "48555407000133"
  )
  @ValidCNPJ(message = "O CNPJ informado é inválido")
  String cnpj,

  @Schema(
    description = "Brazilian mobile phone number with area code (DDD)",
    example = "11988881111"
  )
  @NotBlank(message = "O telefone é obrigatório")
  @ValidPhone(message = "O número de celular informado é inválido")
  String phone,

  @Schema(
    description = "Base delivery fee charged by the restaurant in BRL",
    example = "6.90"
  )
  @NotNull(message = "A taxa base de entrega é obrigatória")
  @DecimalMin(
    value = "0.00",
    message = "A taxa de entrega não pode ser negativa"
  )
  BigDecimal baseDeliveryFee,

  @Schema(description = "Restaurant postal address (CEP and number)")
  @NotNull(message = "O endereço do restaurante é obrigatório")
  @Valid
  AddressRequestDTO address
) {}
