package org.segroup50.financialtracker.config;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserTotpTokenConfigTest {

    private static final String TEST_SECRET_1 = "testSecret123";
    private static final String TEST_SECRET_2 = "anotherSecret456";

    @BeforeEach
    @AfterEach
    void resetSecret() {
        // Clear the secret before and after each test to ensure test isolation
        UserTotpTokenConfig.setTotpSecret(null);
    }

    @Test
    void testGetTotpSecret_InitiallyNull() {
        assertNull(UserTotpTokenConfig.getTotpSecret(),
                "Initial TOTP secret should be null");
    }

    @Test
    void testSetAndGetTotpSecret() {
        // Set first secret
        UserTotpTokenConfig.setTotpSecret(TEST_SECRET_1);
        assertEquals(TEST_SECRET_1, UserTotpTokenConfig.getTotpSecret(),
                "TOTP secret should match the first set value");

        // Change to second secret
        UserTotpTokenConfig.setTotpSecret(TEST_SECRET_2);
        assertEquals(TEST_SECRET_2, UserTotpTokenConfig.getTotpSecret(),
                "TOTP secret should match the second set value");
    }

    @Test
    void testSetTotpSecretToNull() {
        // First set a value
        UserTotpTokenConfig.setTotpSecret(TEST_SECRET_1);

        // Then set to null
        UserTotpTokenConfig.setTotpSecret(null);
        assertNull(UserTotpTokenConfig.getTotpSecret(),
                "TOTP secret should be null after setting to null");
    }
}
