package org.segroup50.financialtracker.service.validation.user;

import org.segroup50.financialtracker.service.validation.BaseValidator;
import org.segroup50.financialtracker.service.validation.ValidationResult;

public class UserValidation extends BaseValidator {
    public static final String USERNAME_PATTERN = "^[a-zA-Z0-9]{4,20}$";
    public static final String EMAIL_PATTERN = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
    public static final String PHONE_PATTERN = "^[0-9]{10,15}$";
    public static final String PASSWORD_PATTERN = "^[a-zA-Z0-9!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>/?]{8,20}$";

    public static ValidationResult validateRegistration(
            String username, String email, String phone,
            String password, String confirmPassword) {
        UserValidation validator = new UserValidation();
        return validator.doValidateRegistration(username, email, phone, password, confirmPassword);
    }

    public static ValidationResult validateLogin(String username, String password) {
        UserValidation validator = new UserValidation();
        return validator.doValidateLogin(username, password);
    }

    private ValidationResult doValidateRegistration(String username, String email, String phone,
                                                    String password, String confirmPassword) {
        ValidationResult result;

        // Check for empty fields
        if ((result = validateFieldNotEmpty(username, "Username")) != null) return result;
        if ((result = validateFieldNotEmpty(email, "Email")) != null) return result;
        if ((result = validateFieldNotEmpty(phone, "Phone number")) != null) return result;
        if ((result = validateFieldNotEmpty(password, "Password")) != null) return result;
        if ((result = validateFieldNotEmpty(confirmPassword, "Confirm Password")) != null) return result;

        // Validate patterns
        if ((result = validatePattern(username, USERNAME_PATTERN,
                "Username must be 4-20 characters long and contain only letters and numbers")) != null) {
            return result;
        }

        if ((result = validatePattern(email, EMAIL_PATTERN,
                "Please enter a valid email address")) != null) {
            return result;
        }

        if ((result = validatePattern(phone, PHONE_PATTERN,
                "Please enter a valid phone number (10-15 digits)")) != null) {
            return result;
        }

        if ((result = validatePattern(password, PASSWORD_PATTERN,
                "Password must be 8-20 characters long and can only contain letters, numbers and special characters")) != null) {
            return result;
        }

        // Validate password match
        if (!password.equals(confirmPassword)) {
            return new ValidationResult(false, "Passwords do not match");
        }

        return new ValidationResult(true, "Validation successful");
    }

    private ValidationResult doValidateLogin(String username, String password) {
        ValidationResult result;

        // Check for empty fields
        if ((result = validateFieldNotEmpty(username, "Username")) != null) return result;
        if ((result = validateFieldNotEmpty(password, "Password")) != null) return result;

        // Validate patterns
        if ((result = validatePattern(username, USERNAME_PATTERN,
                "Invalid username format")) != null) {
            return result;
        }

        if ((result = validatePattern(password, PASSWORD_PATTERN,
                "Invalid password format")) != null) {
            return result;
        }

        return new ValidationResult(true, "Validation successful");
    }
}
