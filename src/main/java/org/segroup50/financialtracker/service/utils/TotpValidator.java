package org.segroup50.financialtracker.service.utils;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.ByteBuffer;
import java.security.GeneralSecurityException;
import java.time.Instant;

/**
 * A utility class for Time-based One-Time Password (TOTP) validation.
 * Implements RFC 6238 (TOTP) standard for generating and validating one-time passwords.
 */
public class TotpValidator {

    private static final int TIME_STEP = 30; // Time step in seconds (typically 30 seconds)
    private static final int CODE_DIGITS = 6; // Number of digits in generated code
    private static final String HMAC_ALGORITHM = "HmacSHA1"; // HMAC algorithm to use

    /**
     * Validates a TOTP code against the shared secret using default parameters.
     *
     * @param secret The shared secret in Base32 encoding
     * @param code The user-provided verification code to validate
     * @return true if the code is valid, false otherwise
     */
    public static boolean validateTotp(String secret, String code) {
        return validateTotp(secret, code, TIME_STEP, CODE_DIGITS);
    }

    /**
     * Validates a TOTP code against the shared secret with custom parameters.
     *
     * @param secret The shared secret in Base32 encoding
     * @param code The user-provided verification code to validate
     * @param timeStep Time step in seconds
     * @param digits Number of digits in the verification code
     * @return true if the code is valid, false otherwise
     */
    public static boolean validateTotp(String secret, String code, int timeStep, int digits) {
        try {
            // Decode Base32 secret key
            byte[] keyBytes = Base32.decode(secret);

            // Calculate current time step
            long timeStepMillis = timeStep * 1000;
            long currentTime = Instant.now().toEpochMilli();
            long timeStepNumber = currentTime / timeStepMillis;

            // Allow for time drift by checking adjacent time windows
            for (int i = -1; i <= 1; i++) {
                String calculatedCode = generateTotp(keyBytes, timeStepNumber + i, digits);
                if (calculatedCode.equals(code)) {
                    return true;
                }
            }

            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Generates a TOTP verification code for the given parameters.
     *
     * @param key The shared secret as byte array
     * @param timeStepNumber The time step number (counter value)
     * @param digits Number of digits in the generated code
     * @return The generated verification code
     * @throws GeneralSecurityException If there's an error in cryptographic operations
     */
    static String generateTotp(byte[] key, long timeStepNumber, int digits)
            throws GeneralSecurityException {
        byte[] data = ByteBuffer.allocate(8).putLong(timeStepNumber).array();

        // Compute HMAC-SHA1 hash
        Mac mac = Mac.getInstance(HMAC_ALGORITHM);
        mac.init(new SecretKeySpec(key, HMAC_ALGORITHM));
        byte[] hash = mac.doFinal(data);

        // Dynamic truncation of the hash value
        int offset = hash[hash.length - 1] & 0xf;
        int binary = ((hash[offset] & 0x7f) << 24) |
                ((hash[offset + 1] & 0xff) << 16) |
                ((hash[offset + 2] & 0xff) << 8) |
                (hash[offset + 3] & 0xff);

        // Generate code with specified number of digits
        int otp = binary % (int) Math.pow(10, digits);
        return String.format("%0" + digits + "d", otp);
    }

    /**
     * Internal Base32 decoding utility class.
     * Implements RFC 4648 Base32 decoding without padding.
     */
    static class Base32 {
        private static final String BASE32_CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ234567";
        private static final int[] BASE32_LOOKUP = new int[256];

        static {
            // Initialize lookup table with -1
            for (int i = 0; i < BASE32_LOOKUP.length; i++) {
                BASE32_LOOKUP[i] = -1;
            }
            // Populate valid characters
            for (int i = 0; i < BASE32_CHARS.length(); i++) {
                BASE32_LOOKUP[BASE32_CHARS.charAt(i)] = i;
            }
        }

        /**
         * Decodes a Base32 encoded string into a byte array.
         *
         * @param encoded The Base32 encoded string
         * @return Decoded byte array
         * @throws IllegalArgumentException If the input contains invalid Base32 characters
         */
        public static byte[] decode(String encoded) {
            // Remove whitespace and padding characters
            encoded = encoded.trim().replaceAll("[=]*$", "");
            encoded = encoded.toUpperCase();

            int length = encoded.length();
            byte[] bytes = new byte[length * 5 / 8];

            int buffer = 0;
            int nextByte = 0;
            int bitsLeft = 0;

            for (char c : encoded.toCharArray()) {
                int value = BASE32_LOOKUP[c];
                if (value < 0) {
                    throw new IllegalArgumentException("Invalid Base32 character: " + c);
                }

                buffer <<= 5;
                buffer |= value;
                bitsLeft += 5;

                if (bitsLeft >= 8) {
                    bytes[nextByte++] = (byte) (buffer >> (bitsLeft - 8));
                    bitsLeft -= 8;
                }
            }

            return bytes;
        }
    }
}
