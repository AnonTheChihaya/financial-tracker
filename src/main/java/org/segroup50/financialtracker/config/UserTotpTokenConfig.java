package org.segroup50.financialtracker.config;

public class UserTotpTokenConfig {
    private static String totpSecret;

    public static String getTotpSecret() {
        return totpSecret;
    }

    public static void setTotpSecret(String secret) {
        totpSecret = secret;
    }
}
