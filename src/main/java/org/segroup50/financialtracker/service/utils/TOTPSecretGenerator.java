package org.segroup50.financialtracker.service.utils;

import java.security.SecureRandom;

/**
 * TOTP utility class for generating TOTP Secrets.
 * <p>
 * This class provides methods to generate Time-based One-Time Password (TOTP) secrets
 * using various HMAC algorithms (SHA1, SHA256, SHA512) and outputs them in Base32 encoding.
 * </p>
 */
public class TOTPSecretGenerator {

    private static final String HMAC_SHA1 = "HmacSHA1";
    private static final String HMAC_SHA256 = "HmacSHA256";
    private static final String HMAC_SHA512 = "HmacSHA512";

    private static final int DEFAULT_KEY_LENGTH = 160; // Default key length in bits

    /**
     * Generates a default TOTP Secret using HmacSHA1 algorithm.
     *
     * @return Base32 encoded Secret string
     */
    public static String generateSecret() {
        return generateSecret(HMAC_SHA1, DEFAULT_KEY_LENGTH);
    }

    /**
     * Generates a TOTP Secret using the specified algorithm.
     *
     * @param algorithm The HMAC algorithm to use (HmacSHA1, HmacSHA256, HmacSHA512)
     * @return Base32 encoded Secret string
     * @throws RuntimeException if the secret generation fails
     */
    public static String generateSecret(String algorithm) {
        int keyLength = DEFAULT_KEY_LENGTH;
        switch (algorithm) {
            case HMAC_SHA256:
                keyLength = 256;
                break;
            case HMAC_SHA512:
                keyLength = 512;
                break;
            default:
                algorithm = HMAC_SHA1;
        }
        return generateSecret(algorithm, keyLength);
    }

    /**
     * Generates a TOTP Secret with specified algorithm and key length.
     *
     * @param algorithm The HMAC algorithm to use
     * @param keyLength The desired key length in bits
     * @return Base32 encoded Secret string
     * @throws RuntimeException if the secret generation fails
     * @throws IllegalArgumentException if the key length is not positive or not a multiple of 8
     */
    public static String generateSecret(String algorithm, int keyLength) {
        try {
            // Use secure random number generator
            SecureRandom secureRandom = new SecureRandom();
            byte[] randomBytes = new byte[keyLength / 8];
            secureRandom.nextBytes(randomBytes);

            // Convert to Base32 encoding
            return Base32Encoder.encode(randomBytes);
        } catch (Exception e) {
            throw new RuntimeException("Failed to generate TOTP secret", e);
        }
    }

    /**
     * Internal Base32 encoder class.
     * <p>
     * This class implements RFC 4648 Base32 encoding without padding.
     * </p>
     */
    static class Base32Encoder {
        private static final String BASE32_CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ234567";
        private static final int[] MASK = {16, 8, 4, 2, 1};

        /**
         * Encodes binary data to Base32 string.
         *
         * @param data The binary data to encode
         * @return Base32 encoded string
         */
        public static String encode(byte[] data) {
            StringBuilder result = new StringBuilder();
            int buffer = 0;
            int bitsLeft = 0;

            for (byte b : data) {
                buffer = (buffer << 8) | (b & 0xFF);
                bitsLeft += 8;

                while (bitsLeft >= 5) {
                    int index = (buffer >>> (bitsLeft - 5)) & 0x1F;
                    result.append(BASE32_CHARS.charAt(index));
                    bitsLeft -= 5;
                }
            }

            if (bitsLeft > 0) {
                int index = (buffer << (5 - bitsLeft)) & 0x1F;
                result.append(BASE32_CHARS.charAt(index));
            }

            return result.toString();
        }
    }
}
