package org.segroup50.financialtracker.service.utils;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class TOTPSecretGeneratorTest {

    @Test
    void testGenerateDefaultSecret() {
        String secret = TOTPSecretGenerator.generateSecret();
        assertNotNull(secret);
        assertFalse(secret.isEmpty());
        // 默认是160位(20字节)，Base32编码后长度应为32字符
        assertEquals(32, secret.length());
    }

    @Test
    void testGenerateSecretWithHmacSHA1() {
        String secret = TOTPSecretGenerator.generateSecret("HmacSHA1");
        assertNotNull(secret);
        assertFalse(secret.isEmpty());
        assertEquals(32, secret.length()); // 160 bits -> 32 Base32 chars
    }

    @Test
    void testGenerateSecretWithHmacSHA256() {
        String secret = TOTPSecretGenerator.generateSecret("HmacSHA256");
        assertNotNull(secret);
        assertFalse(secret.isEmpty());
        assertEquals(52, secret.length()); // 256 bits -> 52 Base32 chars (256/5=51.2, rounded up)
    }

    @Test
    void testGenerateSecretWithInvalidAlgorithm() {
        String secret = TOTPSecretGenerator.generateSecret("InvalidAlgorithm");
        assertNotNull(secret);
        assertFalse(secret.isEmpty());
        // 应该回退到默认的HmacSHA1
        assertEquals(32, secret.length());
    }

    @Test
    void testGenerateSecretWithCustomLength() {
        // 测试自定义长度
        String secret = TOTPSecretGenerator.generateSecret("HmacSHA1", 128);
        assertNotNull(secret);
        assertFalse(secret.isEmpty());
        // 128 bits -> 26 Base32 chars (128/5=25.6, rounded up)
        assertEquals(26, secret.length());
    }

    @Test
    void testBase32Encoder() {
        byte[] testData = "HelloWorld".getBytes();
        String encoded = TOTPSecretGenerator.Base32Encoder.encode(testData);
        assertNotNull(encoded);
        assertFalse(encoded.isEmpty());
        // 简单验证Base32编码结果
        assertTrue(encoded.matches("^[A-Z2-7]+$"));
    }

    @Test
    void testGeneratedSecretsAreDifferent() {
        // 验证两次生成的密钥不同
        String secret1 = TOTPSecretGenerator.generateSecret();
        String secret2 = TOTPSecretGenerator.generateSecret();
        assertNotEquals(secret1, secret2);
    }
}
