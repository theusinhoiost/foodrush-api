package com.foodrush.api.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = CepValidatorConstraint.class)
@Target({ ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidCEP {
  String message() default "O CEP informado é inválido";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}

