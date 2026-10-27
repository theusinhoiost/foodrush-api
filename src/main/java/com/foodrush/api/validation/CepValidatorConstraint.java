package com.foodrush.api.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class CepValidatorConstraint
  implements ConstraintValidator<ValidCEP, String> {

  private static final String CEP_REGEX = "^\\d{5}-?\\d{3}$";

  @Override
  public boolean isValid(String cep, ConstraintValidatorContext context) {
    if (cep == null || cep.isBlank()) {
      return false;
    }
    return cep.trim().matches(CEP_REGEX);
  }
}

