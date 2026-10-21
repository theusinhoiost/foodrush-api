package com.foodrush.api.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class CnpjValidatorConstraint implements ConstraintValidator<ValidCNPJ, String> {

    @Override
    public boolean isValid(String cnpj, ConstraintValidatorContext context) {
        if (cnpj == null) {
            return false;
        }

        // Remove caracteres não numéricos (pontos, barras e traços)
        cnpj = cnpj.replaceAll("\\D", "");

        // CNPJ deve ter exatamente 14 dígitos e não pode ser uma sequência de números iguais
        if (cnpj.length() != 14 || cnpj.matches("(\\d)\\1{13}")) {
            return false;
        }

        try {
            // Cálculo do 1º dígito verificador
            int soma = 0;
            int peso = 2;
            for (int i = 11; i >= 0; i--) {
                soma += Character.getNumericValue(cnpj.charAt(i)) * peso;
                peso = (peso < 9) ? peso + 1 : 2;
            }
            int digito1 = 11 - (soma % 11);
            digito1 = (digito1 > 9) ? 0 : digito1;

            // Cálculo do 2º dígito verificador
            soma = 0;
            peso = 2;
            for (int i = 12; i >= 0; i--) {
                soma += Character.getNumericValue(cnpj.charAt(i)) * peso;
                peso = (peso < 9) ? peso + 1 : 2;
            }
            int digito2 = 11 - (soma % 11);
            digito2 = (digito2 > 9) ? 0 : digito2;

            // Compara os dígitos calculados com os dígitos informados
            return digito1 == Character.getNumericValue(cnpj.charAt(12)) &&
                   digito2 == Character.getNumericValue(cnpj.charAt(13));
        } catch (NumberFormatException e) {
            return false;
        }
    }
}