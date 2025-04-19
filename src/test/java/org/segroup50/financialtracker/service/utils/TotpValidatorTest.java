package org.segroup50.financialtracker.service.utils;

import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

class TotpValidatorTest {

    // Test secret (Base32 encoded)
    private static final String TEST_SECRET = "JBSWY3DPEHPK3PXP";

    @Test
    void testValidateTotpWithDefaultParameters() {
        // This test is tricky because TOTP is time-based
        // For demonstration, we'll test that invalid codes fail
        assertFalse(TotpValidator.validateTotp(TEST_SECRET, "000000"));
        assertFalse(TotpValidator.validateTotp(TEST_SECRET, "123456"));
    }

    @Test
    void testValidateTotpWithCustomParameters() {
        // Similarly, this would need proper time mocking
        assertFalse(TotpValidator.validateTotp(TEST_SECRET, "000000", 30, 6));
        assertFalse(TotpValidator.validateTotp(TEST_SECRET, "123456", 30, 6));
    }

    @Test
    void testBase32Decoding() {
        String encoded = "JBSWY3DPEHPK3PXP";
        byte[] decoded = TotpValidator.Base32.decode(encoded);
        assertNotNull(decoded);
        assertTrue(decoded.length > 0);
    }

    @Test
    void testBase32DecodingWithInvalidChars() {
        assertThrows(IllegalArgumentException.class, () -> {
            TotpValidator.Base32.decode("INVALID1!");
        });
    }

    @Test
    void testGenerateTotp() throws Exception {
        byte[] key = TotpValidator.Base32.decode(TEST_SECRET);
        long timeStep = Instant.now().toEpochMilli() / (30 * 1000);

        String code = TotpValidator.generateTotp(key, timeStep, 6);
        assertNotNull(code);
        assertEquals(6, code.length());
        assertTrue(code.matches("\\d{6}"));
    }
}
