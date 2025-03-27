package org.segroup50.financialtracker.service.utils;

import java.security.SecureRandom;

public class TOTPSecretGenerator {

    private static final String HMAC_SHA1 = "HmacSHA1";
    private static final String HMAC_SHA256 = "HmacSHA256";
    private static final String HMAC_SHA512 = "HmacSHA512";

    private static final int DEFAULT_KEY_LENGTH = 160;

    public static String generateSecret() {
        return generateSecret(HMAC_SHA1, DEFAULT_KEY_LENGTH);
    }

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

    public static String generateSecret(String algorithm, int keyLength) {
        try {
            SecureRandom secureRandom = new SecureRandom();
            byte[] randomBytes = new byte[keyLength / 8];
            secureRandom.nextBytes(randomBytes);

            return Base32Encoder.encode(randomBytes);
        } catch (Exception e) {
            throw new RuntimeException("Failed to generate TOTP secret", e);
        }
    }

    static class Base32Encoder {
        private static final String BASE32_CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ234567";
        private static final int[] MASK = {16, 8, 4, 2, 1};

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
