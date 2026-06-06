package org.spring.finalproject.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.regex.Pattern;

public class PasswordPolicyValidator
        implements ConstraintValidator<PasswordPolicy, String> {

    private static final Pattern POLICY = Pattern.compile(
            "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&#])[A-Za-z\\d@$!%*?&#]{8,}$");

    @Override
    public boolean isValid(String value,
                           ConstraintValidatorContext context) {

        if (value == null || value.isBlank()) {
            return true;
        }

        return POLICY.matcher(value).matches();
    }
}
