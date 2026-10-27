package com.foodrush.api.dto.response;

import com.foodrush.api.model.User;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import java.time.OffsetDateTime;
import java.util.UUID;

@Schema(description = "Response representation of a registered user")
public record UserResponseDTO(
  @Schema(
    description = "Unique UUID of the user",
    example = "11111111-1111-1111-1111-111111111111"
  )
  UUID id,

  @Schema(description = "Full name of the user", example = "Matheus Cliente")
  String name,

  @Schema(description = "Email address", example = "cliente@foodrush.com")
  String email,

  @Schema(description = "Mobile phone number", example = "11991112222")
  String phone,

  @Schema(description = "11-digit CPF", example = "52998224725")
  String cpf,

  @Schema(description = "Assigned role", example = "CLIENT")
  String role,

  @Schema(description = "Whether the account is active", example = "true")
  Boolean active,

  @Schema(description = "User's resolved address")
  AddressResponseDTO address,

  @Schema(
    description = "Timestamp when the user account was created",
    example = "2026-10-27T15:00:00Z"
  )
  OffsetDateTime createdAt
) implements Serializable {
  public static UserResponseDTO fromEntity(User user) {
    return new UserResponseDTO(
      user.getId(),
      user.getName(),
      user.getEmail(),
      user.getPhone(),
      user.getCpf(),
      user.getRole().name(),
      user.getActive(),
      AddressResponseDTO.fromEntity(user.getAddress()),
      user.getCreatedAt()
    );
  }
}
