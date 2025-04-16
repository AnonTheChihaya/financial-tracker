package org.segroup50.financialtracker.service.validation.transaction;

import org.junit.jupiter.api.Test;
import org.segroup50.financialtracker.service.validation.ValidationResult;

import static org.junit.jupiter.api.Assertions.*;

class TransactionValidationTest {

    @Test
    void validateTransactionInput_AllValidInput_ReturnsSuccess() {
        ValidationResult result = TransactionValidation.validateTransactionInput(
                "2023-05-15",
                "100.50",
                "Expense",
                "Food",
                "Cash",
                "Lunch at restaurant"
        );
        assertTrue(result.isValid());
        assertEquals("Validation successful", result.getMessage());
    }

    @Test
    void validateTransactionInput_EmptyDate_ReturnsError() {
        ValidationResult result = TransactionValidation.validateTransactionInput(
                "",
                "100.50",
                "Expense",
                "Food",
                "Cash",
                "Lunch"
        );
        assertFalse(result.isValid());
        assertEquals("Transaction date cannot be empty", result.getMessage());
    }

    @Test
    void validateTransactionInput_InvalidDateFormat_ReturnsError() {
        ValidationResult result = TransactionValidation.validateTransactionInput(
                "15-05-2023",
                "100.50",
                "Expense",
                "Food",
                "Cash",
                "Lunch"
        );
        assertFalse(result.isValid());
        assertEquals("Date must be in yyyy-mm-dd format", result.getMessage());
    }

    @Test
    void validateTransactionInput_EmptyAmount_ReturnsError() {
        ValidationResult result = TransactionValidation.validateTransactionInput(
                "2023-05-15",
                "",
                "Expense",
                "Food",
                "Cash",
                "Lunch"
        );
        assertFalse(result.isValid());
        assertEquals("Amount cannot be empty", result.getMessage());
    }

    @Test
    void validateTransactionInput_InvalidAmountFormat_ReturnsError() {
        ValidationResult result = TransactionValidation.validateTransactionInput(
                "2023-05-15",
                "100.555",
                "Expense",
                "Food",
                "Cash",
                "Lunch"
        );
        assertFalse(result.isValid());
        assertEquals("Please enter a valid amount (e.g. 100 or 100.50)", result.getMessage());
    }

    @Test
    void validateTransactionInput_EmptyType_ReturnsError() {
        ValidationResult result = TransactionValidation.validateTransactionInput(
                "2023-05-15",
                "100.50",
                "",
                "Food",
                "Cash",
                "Lunch"
        );
        assertFalse(result.isValid());
        assertEquals("Transaction type cannot be empty", result.getMessage());
    }

    @Test
    void validateTransactionInput_EmptyCategory_ReturnsError() {
        ValidationResult result = TransactionValidation.validateTransactionInput(
                "2023-05-15",
                "100.50",
                "Expense",
                "",
                "Cash",
                "Lunch"
        );
        assertFalse(result.isValid());
        assertEquals("Category cannot be empty", result.getMessage());
    }

    @Test
    void validateTransactionInput_EmptyAccount_ReturnsError() {
        ValidationResult result = TransactionValidation.validateTransactionInput(
                "2023-05-15",
                "100.50",
                "Expense",
                "Food",
                "",
                "Lunch"
        );
        assertFalse(result.isValid());
        assertEquals("Account cannot be empty", result.getMessage());
    }

    @Test
    void validateTransactionInput_EmptyNotes_ReturnsSuccess() {
        ValidationResult result = TransactionValidation.validateTransactionInput(
                "2023-05-15",
                "100.50",
                "Expense",
                "Food",
                "Cash",
                "1"
        );
        assertTrue(result.isValid());
        assertEquals("Validation successful", result.getMessage());
    }

    @Test
    void validateTransactionInput_InvalidNotes_ReturnsError() {
        ValidationResult result = TransactionValidation.validateTransactionInput(
                "2023-05-15",
                "100.50",
                "Expense",
                "Food",
                "Cash",
                "This note is way too long and exceeds the maximum allowed characters. " +
                        "This note is way too long and exceeds the maximum allowed characters. " +
                        "This note is way too long and exceeds the maximum allowed characters. " +
                        "This note is way too long and exceeds the maximum allowed characters."
        );
        assertFalse(result.isValid());
        assertEquals("Notes can only contain letters, numbers, spaces, and basic punctuation (max 200 characters)",
                result.getMessage());
    }

    @Test
    void validateTransactionInput_NotesWithInvalidCharacters_ReturnsError() {
        ValidationResult result = TransactionValidation.validateTransactionInput(
                "2023-05-15",
                "100.50",
                "Expense",
                "Food",
                "Cash",
                "Invalid@note#with$symbols"
        );
        assertFalse(result.isValid());
        assertEquals("Notes can only contain letters, numbers, spaces, and basic punctuation (max 200 characters)",
                result.getMessage());
    }
}
