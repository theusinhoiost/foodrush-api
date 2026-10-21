package com.foodrush.api.dto.request;

import com.foodrush.api.validation.ValidCPF;
import com.foodrush.api.validation.ValidPhone;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserRequestDTO(
  @NotBlank(message = "O nome é obrigatório")
  @Size(max = 100, message = "O nome deve ter no máximo 100 caracteres")
  String name,

  @NotBlank(message = "O e-mail é obrigatório")
  @Email(message = "Formato de e-mail inválido")
  @Size(max = 100, message = "O e-mail deve ter no máximo 100 caracteres")
  String email,

  @NotBlank(message = "A senha é obrigatória")
  @Size(min = 6, message = "A senha deve ter pelo menos 6 caracteres")
  String password,

  @NotBlank(message = "O telefone é obrigatório")
  @ValidPhone(message = "O número de celular informado é inválido")
  String phone,

  @ValidCPF(message = "O CPF informado é inválido") String cpf,

  @NotBlank(message = "O perfil (role) é obrigatório")
  @Size(max = 20, message = "O perfil deve ter no máximo 20 caracteres")
  String role // Ex: CUSTOMER, RESTAURANT_OWNER, DELIVERY_DRIVER
) {}
