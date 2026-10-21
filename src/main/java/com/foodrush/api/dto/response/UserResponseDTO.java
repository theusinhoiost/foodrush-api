package com.foodrush.api.dto.response;

import com.foodrush.api.model.User;
import java.time.OffsetDateTime;
import java.util.UUID;

public record UserResponseDTO(
  UUID id,
  String name,
  String email,
  String phone,
  String cpf,
  String role,
  Boolean active,
  OffsetDateTime createdAt
) {
  public static UserResponseDTO fromEntity(User user) {
    return new UserResponseDTO(
      user.getId(),
      user.getName(),
      user.getEmail(),
      user.getPhone(),
      user.getCpf(),
      user.getRole().name(),
      user.getActive(),
      user.getCreatedAt()
    );
  }
}
