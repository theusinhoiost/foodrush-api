package com.foodrush.api.dto.response;

import com.foodrush.api.model.Restaurant;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.UUID;

@Schema(description = "Response representation of a registered restaurant")
public record RestaurantResponseDTO(
  @Schema(
    description = "Unique UUID of the restaurant",
    example = "bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbb1"
  )
  UUID id,

  @Schema(
    description = "UUID of the owner user",
    example = "22222222-2222-2222-2222-222222222222"
  )
  UUID ownerId,

  @Schema(
    description = "Commercial trade name",
    example = "FoodRush Burger Artesanal"
  )
  String tradeName,

  @Schema(description = "14-digit CNPJ", example = "48555407000133")
  String cnpj,

  @Schema(description = "Base delivery fee in BRL", example = "6.90")
  BigDecimal baseDeliveryFee,

  @Schema(description = "Geographic latitude", example = "-23.550520")
  BigDecimal latitude,

  @Schema(description = "Geographic longitude", example = "-46.633309")
  BigDecimal longitude,

  @Schema(description = "Complete resolved postal address")
  AddressResponseDTO address,

  @Schema(
    description = "Whether the restaurant is currently open for orders",
    example = "true"
  )
  Boolean isOpen
) implements Serializable {
  public static RestaurantResponseDTO fromEntity(Restaurant restaurant) {
    return new RestaurantResponseDTO(
      restaurant.getId(),
      restaurant.getOwner().getId(),
      restaurant.getTradeName(),
      restaurant.getCnpj(),
      restaurant.getBaseDeliveryFee(),
      restaurant.getLatitude(),
      restaurant.getLongitude(),
      AddressResponseDTO.fromEntity(restaurant.getAddress()),
      restaurant.getIsOpen()
    );
  }
}