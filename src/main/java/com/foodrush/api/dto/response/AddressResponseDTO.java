package com.foodrush.api.dto.response;

import com.foodrush.api.model.Address;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.UUID;

@Schema(
  description = "Complete address details including street information and geographic coordinates resolved by the backend"
)
public record AddressResponseDTO(
  @Schema(
    description = "Unique identifier of the address",
    example = "a0000000-0000-0000-0000-000000000002"
  )
  UUID id,

  @Schema(description = "8-digit Brazilian postal code (CEP)", example = "01001000")
  String cep,

  @Schema(description = "Street name", example = "Praça da Sé")
  String street,

  @Schema(description = "Street number", example = "100")
  String number,

  @Schema(description = "Address complement", example = "Loja 1")
  String complement,

  @Schema(description = "Neighborhood / district", example = "Sé")
  String neighborhood,

  @Schema(description = "City name", example = "São Paulo")
  String city,

  @Schema(description = "Two-letter state code (UF)", example = "SP")
  String state,

  @Schema(description = "Resolved geographic latitude", example = "-23.550520")
  BigDecimal latitude,

  @Schema(description = "Resolved geographic longitude", example = "-46.633309")
  BigDecimal longitude
) implements Serializable {
  public static AddressResponseDTO fromEntity(Address address) {
    if (address == null) {
      return null;
    }
    return new AddressResponseDTO(
      address.getId(),
      address.getCep(),
      address.getStreet(),
      address.getNumber(),
      address.getComplement(),
      address.getNeighborhood(),
      address.getCity(),
      address.getState(),
      address.getLatitude(),
      address.getLongitude()
    );
  }
}

