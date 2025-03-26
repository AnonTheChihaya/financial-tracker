package org.segroup50.financialtracker.service.validation;

import java.util.regex.Pattern;

public abstract class BaseValidator {
    protected boolean isNullOrEmpty(String value) {
        return value == null || value.trim().isEmpty();
    }

    protected boolean matchesPattern(String value, String pattern) {
        return Pattern.matches(pattern, value);
    }

    protected ValidationResult validateFieldNotEmpty(String value, String fieldName) {
        if (isNullOrEmpty(value)) {
            return new ValidationResult(false, fieldName + " cannot be empty");
        }
        return null;
    }

    protected ValidationResult validatePattern(String value, String pattern, String errorMessage) {
        if (!matchesPattern(value, pattern)) {
            return new ValidationResult(false, errorMessage);
        }
        return null;
    }
}
