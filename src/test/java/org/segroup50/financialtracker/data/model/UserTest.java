package org.segroup50.financialtracker.data.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    @Test
    void testUserConstructorAndGetters() {
        // Test data
        String id = "user123";
        String username = "testuser";
        String email = "test@example.com";
        String phone = "1234567890";
        String pwd = "securePassword123";
        String totpsecret = "TOTPSecretKey";

        // Create user object
        User user = new User(id, username, email, phone, pwd, totpsecret);

        // Verify all getters return expected values
        assertEquals(id, user.getId());
        assertEquals(username, user.getUsername());
        assertEquals(email, user.getEmail());
        assertEquals(phone, user.getPhone());
        assertEquals(pwd, user.getPwd());
        assertEquals(totpsecret, user.getTotpsecret());
    }

    @Test
    void testEmptyConstructorAndSetters() {
        // Create empty user
        User user = new User();

        // Test data
        String id = "user456";
        String username = "anotheruser";
        String email = "another@test.com";
        String phone = "9876543210";
        String pwd = "anotherPassword";
        String totpsecret = "AnotherTOTPKey";

        // Set values
        user.setId(id);
        user.setUsername(username);
        user.setEmail(email);
        user.setPhone(phone);
        user.setPwd(pwd);
        user.setTotpsecret(totpsecret);

        // Verify all getters return the set values
        assertEquals(id, user.getId());
        assertEquals(username, user.getUsername());
        assertEquals(email, user.getEmail());
        assertEquals(phone, user.getPhone());
        assertEquals(pwd, user.getPwd());
        assertEquals(totpsecret, user.getTotpsecret());
    }

    @Test
    void testToString() {
        // Test data
        String id = "user789";
        String username = "stringtestuser";
        String email = "stringtest@example.com";
        String phone = "5551234567";
        String pwd = "testPassword";
        String totpsecret = "TestTOTPKey";

        // Create user
        User user = new User(id, username, email, phone, pwd, totpsecret);

        // Expected toString output
        String expectedToString = "User{" +
                "id='" + id + '\'' +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", pwd='" + pwd + '\'' +
                ", totpsecret='" + totpsecret + '\'' +
                '}';

        // Verify toString output
        assertEquals(expectedToString, user.toString());
    }
}
