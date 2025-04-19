package org.segroup50.financialtracker.service.validation.user;

import org.junit.jupiter.api.Test;
import org.segroup50.financialtracker.service.validation.ValidationResult;

import static org.junit.jupiter.api.Assertions.*;

class UserValidationTest {

    @Test
    void validateRegistration_Success() {
        ValidationResult result = UserValidation.validateRegistration(
                "validUser123",
                "test@example.com",
                "1234567890",
                "Password123!",
                "Password123!"
        );
        assertTrue(result.isValid());
        assertEquals("Validation successful", result.getMessage());
    }

    @Test
    void validateRegistration_EmptyUsername() {
        ValidationResult result = UserValidation.validateRegistration(
                "",
                "test@example.com",
                "1234567890",
                "Password123!",
                "Password123!"
        );
        assertFalse(result.isValid());
        assertEquals("Username cannot be empty", result.getMessage());
    }

    @Test
    void validateRegistration_InvalidUsernameFormat() {
        ValidationResult result = UserValidation.validateRegistration(
                "usr@", // contains special character
                "test@example.com",
                "1234567890",
                "Password123!",
                "Password123!"
        );
        assertFalse(result.isValid());
        assertEquals("Username must be 4-20 characters long and contain only letters and numbers", result.getMessage());
    }

    @Test
    void validateRegistration_InvalidEmailFormat() {
        ValidationResult result = UserValidation.validateRegistration(
                "validUser123",
                "invalid-email",
                "1234567890",
                "Password123!",
                "Password123!"
        );
        assertFalse(result.isValid());
        assertEquals("Please enter a valid email address", result.getMessage());
    }

    @Test
    void validateRegistration_InvalidPhoneFormat() {
        ValidationResult result = UserValidation.validateRegistration(
                "validUser123",
                "test@example.com",
                "123", // too short
                "Password123!",
                "Password123!"
        );
        assertFalse(result.isValid());
        assertEquals("Please enter a valid phone number (10-15 digits)", result.getMessage());
    }

    @Test
    void validateRegistration_InvalidPasswordFormat() {
        ValidationResult result = UserValidation.validateRegistration(
                "validUser123",
                "test@example.com",
                "1234567890",
                "short", // too short
                "short"
        );
        assertFalse(result.isValid());
        assertEquals("Password must be 8-20 characters long and can only contain letters, numbers and special characters", result.getMessage());
    }

    @Test
    void validateRegistration_PasswordMismatch() {
        ValidationResult result = UserValidation.validateRegistration(
                "validUser123",
                "test@example.com",
                "1234567890",
                "Password123!",
                "DifferentPassword123!"
        );
        assertFalse(result.isValid());
        assertEquals("Passwords do not match", result.getMessage());
    }

    @Test
    void validateLogin_Success() {
        ValidationResult result = UserValidation.validateLogin(
                "validUser123",
                "Password123!"
        );
        assertTrue(result.isValid());
        assertEquals("Validation successful", result.getMessage());
    }

    @Test
    void validateLogin_EmptyUsername() {
        ValidationResult result = UserValidation.validateLogin(
                "",
                "Password123!"
        );
        assertFalse(result.isValid());
        assertEquals("Username cannot be empty", result.getMessage());
    }

    @Test
    void validateLogin_EmptyPassword() {
        ValidationResult result = UserValidation.validateLogin(
                "validUser123",
                ""
        );
        assertFalse(result.isValid());
        assertEquals("Password cannot be empty", result.getMessage());
    }

    @Test
    void validateLogin_InvalidUsernameFormat() {
        ValidationResult result = UserValidation.validateLogin(
                "usr@", // contains special character
                "Password123!"
        );
        assertFalse(result.isValid());
        assertEquals("Invalid username format", result.getMessage());
    }

    @Test
    void validateLogin_InvalidPasswordFormat() {
        ValidationResult result = UserValidation.validateLogin(
                "validUser123",
                "short" // too short
        );
        assertFalse(result.isValid());
        assertEquals("Invalid password format", result.getMessage());
    }
}
