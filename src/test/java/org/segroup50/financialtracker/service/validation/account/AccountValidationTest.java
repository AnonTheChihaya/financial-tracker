package org.segroup50.financialtracker.service.validation.account;

import org.junit.jupiter.api.Test;
import org.segroup50.financialtracker.service.validation.ValidationResult;

import static org.junit.jupiter.api.Assertions.*;

class AccountValidationTest {

    @Test
    void validateAccountInput_AllValidInputs_ReturnsSuccess() {
        ValidationResult result = AccountValidation.validateAccountInput(
                "Savings Account", "Bank", "1000.50");
        assertTrue(result.isValid());
        assertEquals("Validation successful", result.getMessage());
    }

    @Test
    void validateAccountInput_EmptyName_ReturnsError() {
        ValidationResult result = AccountValidation.validateAccountInput(
                "", "Bank", "1000.50");
        assertFalse(result.isValid());
        assertEquals("Account name cannot be empty", result.getMessage());
    }

    @Test
    void validateAccountInput_EmptyType_ReturnsError() {
        ValidationResult result = AccountValidation.validateAccountInput(
                "Savings Account", "", "1000.50");
        assertFalse(result.isValid());
        assertEquals("Account type cannot be empty", result.getMessage());
    }

    @Test
    void validateAccountInput_EmptyBalance_ReturnsError() {
        ValidationResult result = AccountValidation.validateAccountInput(
                "Savings Account", "Bank", "");
        assertFalse(result.isValid());
        assertEquals("Balance cannot be empty", result.getMessage());
    }

    @Test
    void validateAccountInput_InvalidNamePattern_ReturnsError() {
        ValidationResult result = AccountValidation.validateAccountInput(
                "S@vings", "Bank", "1000.50");
        assertFalse(result.isValid());
        assertEquals("Account name must be 3-50 characters long and contain only letters, numbers and spaces",
                result.getMessage());
    }

    @Test
    void validateAccountInput_NameTooShort_ReturnsError() {
        ValidationResult result = AccountValidation.validateAccountInput(
                "AB", "Bank", "1000.50");
        assertFalse(result.isValid());
        assertEquals("Account name must be 3-50 characters long and contain only letters, numbers and spaces",
                result.getMessage());
    }

    @Test
    void validateAccountInput_InvalidTypePattern_ReturnsError() {
        ValidationResult result = AccountValidation.validateAccountInput(
                "Savings Account", "Bank1", "1000.50");
        assertFalse(result.isValid());
        assertEquals("Account type must be 3-30 characters long and contain only letters and spaces",
                result.getMessage());
    }

    @Test
    void validateAccountInput_TypeTooShort_ReturnsError() {
        ValidationResult result = AccountValidation.validateAccountInput(
                "Savings Account", "B", "1000.50");
        assertFalse(result.isValid());
        assertEquals("Account type must be 3-30 characters long and contain only letters and spaces",
                result.getMessage());
    }

    @Test
    void validateAccountInput_InvalidBalancePattern_ReturnsError() {
        ValidationResult result = AccountValidation.validateAccountInput(
                "Savings Account", "Bank", "1000.555");
        assertFalse(result.isValid());
        assertEquals("Please enter a valid balance amount (e.g. 100 or 100.50)",
                result.getMessage());
    }

    @Test
    void validateAccountInput_NullName_ReturnsError() {
        ValidationResult result = AccountValidation.validateAccountInput(
                null, "Bank", "1000.50");
        assertFalse(result.isValid());
        assertEquals("Account name cannot be empty", result.getMessage());
    }

    @Test
    void validateAccountInput_NullType_ReturnsError() {
        ValidationResult result = AccountValidation.validateAccountInput(
                "Savings Account", null, "1000.50");
        assertFalse(result.isValid());
        assertEquals("Account type cannot be empty", result.getMessage());
    }

    @Test
    void validateAccountInput_NullBalance_ReturnsError() {
        ValidationResult result = AccountValidation.validateAccountInput(
                "Savings Account", "Bank", null);
        assertFalse(result.isValid());
        assertEquals("Balance cannot be empty", result.getMessage());
    }

    @Test
    void validateAccountInput_ValidBalanceWithoutDecimal_ReturnsSuccess() {
        ValidationResult result = AccountValidation.validateAccountInput(
                "Savings Account", "Bank", "1000");
        assertTrue(result.isValid());
        assertEquals("Validation successful", result.getMessage());
    }
}
