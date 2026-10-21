package com.foodrush.api.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PhoneValidatorConstraint implements ConstraintValidator<ValidPhone, String> {

    // Regex para validar celular brasileiro:
    // Opcionalmente com código de país (+55), com DDD (2 dígitos) e número começando com 9 (celular)
    private static final String PHONE_REGEX = "^(?:\\+?55\\s?)?(?:\\(?[1-9]{2}\\)?\\s?)?(?:9[0-9]{4})-?[0-9]{4}$";

    @Override
    public boolean isValid(String phone, ConstraintValidatorContext context) {
        if (phone == null || phone.isBlank()) {
            return false;
        }

        // Remove todos os caracteres não numéricos para validar apenas os dígitos
        String cleanPhone = phone.replaceAll("\\D", "");

        // Um celular brasileiro válido com DDD deve ter 10 ou 11 dígitos (ex: 11988887777)
        if (cleanPhone.length() != 10 && cleanPhone.length() != 11) {
            return false;
        }

        // Verifica se o número de celular corresponde ao padrão
        return phone.matches(PHONE_REGEX);
    }
}