// TransactionValidation.java
package org.segroup50.financialtracker.service.validation.transaction;

import org.segroup50.financialtracker.service.validation.BaseValidator;
import org.segroup50.financialtracker.service.validation.ValidationResult;

public class TransactionValidation extends BaseValidator {
    private static final String DATE_PATTERN = "^\\d{4}-(0[1-9]|1[0-2])-(0[1-9]|[12][0-9]|3[01])$";
    private static final String AMOUNT_PATTERN = "^\\d+(\\.\\d{1,2})?$";
    private static final String NOTES_PATTERN = "^[a-zA-Z0-9\\s.,!?-]{0,200}$";

    public static ValidationResult validateTransactionInput(
            String date, String amount, String type,
            String category, String account, String notes) {
        TransactionValidation validator = new TransactionValidation();
        return validator.doValidate(date, amount, type, category, account, notes);
    }

    private ValidationResult doValidate(String date, String amount, String type,
                                        String category, String account, String notes) {
        ValidationResult result;

        // Check for empty fields
        if ((result = validateFieldNotEmpty(date, "Transaction date")) != null) return result;
        if ((result = validateFieldNotEmpty(amount, "Amount")) != null) return result;
        if ((result = validateFieldNotEmpty(type, "Transaction type")) != null) return result;
        if ((result = validateFieldNotEmpty(category, "Category")) != null) return result;
        if ((result = validateFieldNotEmpty(account, "Account")) != null) return result;
        if ((result = validateFieldNotEmpty(notes, "Notes")) != null) return result;

        // Validate patterns
        if ((result = validatePattern(date, DATE_PATTERN,
                "Date must be in yyyy-mm-dd format")) != null) {
            return result;
        }

        if ((result = validatePattern(amount, AMOUNT_PATTERN,
                "Please enter a valid amount (e.g. 100 or 100.50)")) != null) {
            return result;
        }

        // Notes are optional but have constraints if provided
        if (!isNullOrEmpty(notes) &&
                (result = validatePattern(notes, NOTES_PATTERN,
                        "Notes can only contain letters, numbers, spaces, and basic punctuation (max 200 characters)")) != null) {
            return result;
        }

        return new ValidationResult(true, "Validation successful");
    }
}
