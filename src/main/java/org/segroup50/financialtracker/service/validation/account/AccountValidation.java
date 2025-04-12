// AccountValidation.java
package org.segroup50.financialtracker.service.validation.account;

import org.segroup50.financialtracker.service.validation.BaseValidator;
import org.segroup50.financialtracker.service.validation.ValidationResult;

public class AccountValidation extends BaseValidator {
    private static final String ACCOUNT_NAME_PATTERN = "^[a-zA-Z0-9\\s]{3,50}$";
    private static final String ACCOUNT_TYPE_PATTERN = "^[a-zA-Z\\s]{3,30}$";
    private static final String BALANCE_PATTERN = "^\\d+(\\.\\d{1,2})?$";

    public static ValidationResult validateAccountInput(String name, String type, String balance) {
        AccountValidation validator = new AccountValidation();
        return validator.doValidate(name, type, balance);
    }

    private ValidationResult doValidate(String name, String type, String balance) {
        ValidationResult result;

        // Check for empty fields
        if ((result = validateFieldNotEmpty(name, "Account name")) != null) return result;
        if ((result = validateFieldNotEmpty(type, "Account type")) != null) return result;
        if ((result = validateFieldNotEmpty(balance, "Balance")) != null) return result;

        // Validate patterns
        if ((result = validatePattern(name, ACCOUNT_NAME_PATTERN,
                "Account name must be 3-50 characters long and contain only letters, numbers and spaces")) != null) {
            return result;
        }

        if ((result = validatePattern(type, ACCOUNT_TYPE_PATTERN,
                "Account type must be 3-30 characters long and contain only letters and spaces")) != null) {
            return result;
        }

        if ((result = validatePattern(balance, BALANCE_PATTERN,
                "Please enter a valid balance amount (e.g. 100 or 100.50)")) != null) {
            return result;
        }

        return new ValidationResult(true, "Validation successful");
    }
}
