package com.foodrush.api.dto.request;

import com.foodrush.api.validation.ValidCNPJ;
import com.foodrush.api.validation.ValidPhone;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.UUID;

public record RestaurantRequestDTO(
  @NotNull(message = "O ID do proprietário é obrigatório") UUID ownerId,

  @NotBlank(message = "O nome fantasia é obrigatório")
  @Size(
    max = 120,
    message = "O nome fantasia deve ter no máximo 120 caracteres"
  )
  String tradeName,

  @ValidCNPJ(message = "O CNPJ informado é inválido") String cnpj,

  @NotBlank(message = "O telefone é obrigatório")
  @ValidPhone(message = "O número de celular informado é inválido")
  String phone,

  @NotNull(message = "A taxa base de entrega é obrigatória")
  @DecimalMin(
    value = "0.00",
    message = "A taxa de entrega não pode ser negativa"
  )
  BigDecimal baseDeliveryFee,

  @NotNull(message = "A latitude é obrigatória") BigDecimal latitude,

  @NotNull(message = "A longitude é obrigatória") BigDecimal longitude
) {}
