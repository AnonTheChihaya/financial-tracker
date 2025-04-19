package org.segroup50.financialtracker.config;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.segroup50.financialtracker.data.model.User;

import static org.junit.jupiter.api.Assertions.*;

class CurrentUserConfigTest {

    private User testUser;

    @BeforeEach
    void setUp() {
        // Create a test user before each test
        testUser = new User(
                "user123",
                "testuser",
                "test@example.com",
                "1234567890",
                "password123",
                "totpsecret123"
        );
    }

    @AfterEach
    void tearDown() {
        // Clear current user after each test
        CurrentUserConfig.clearCurrentUser();
    }

    @Test
    void testSetAndGetCurrentUser() {
        // Set the current user
        CurrentUserConfig.setCurrentUser(testUser);

        // Get the current user and verify
        User currentUser = CurrentUserConfig.getCurrentUser();
        assertNotNull(currentUser);
        assertEquals("user123", currentUser.getId());
        assertEquals("testuser", currentUser.getUsername());
    }

    @Test
    void testClearCurrentUser() {
        // Set the current user first
        CurrentUserConfig.setCurrentUser(testUser);
        assertNotNull(CurrentUserConfig.getCurrentUser());

        // Clear the current user
        CurrentUserConfig.clearCurrentUser();

        // Verify user is cleared
        assertNull(CurrentUserConfig.getCurrentUser());
    }

    @Test
    void testIsUserLoggedIn() {
        // Initially no user logged in
        assertFalse(CurrentUserConfig.isUserLoggedIn());

        // Set a user
        CurrentUserConfig.setCurrentUser(testUser);
        assertTrue(CurrentUserConfig.isUserLoggedIn());

        // Clear user
        CurrentUserConfig.clearCurrentUser();
        assertFalse(CurrentUserConfig.isUserLoggedIn());
    }

    @Test
    void testGetCurrentUserId() {
        // Initially no user ID
        assertNull(CurrentUserConfig.getCurrentUserId());

        // Set a user
        CurrentUserConfig.setCurrentUser(testUser);
        assertEquals("user123", CurrentUserConfig.getCurrentUserId());
    }

    @Test
    void testGetCurrentUsername() {
        // Initially no username
        assertNull(CurrentUserConfig.getCurrentUsername());

        // Set a user
        CurrentUserConfig.setCurrentUser(testUser);
        assertEquals("testuser", CurrentUserConfig.getCurrentUsername());
    }

    @Test
    void testUserGettersAndSetters() {
        User user = new User();

        // Test setters and getters
        user.setId("newId");
        assertEquals("newId", user.getId());

        user.setUsername("newUser");
        assertEquals("newUser", user.getUsername());

        user.setEmail("new@email.com");
        assertEquals("new@email.com", user.getEmail());

        user.setPhone("9876543210");
        assertEquals("9876543210", user.getPhone());

        user.setPwd("newPassword");
        assertEquals("newPassword", user.getPwd());

        user.setTotpsecret("newTotpSecret");
        assertEquals("newTotpSecret", user.getTotpsecret());
    }

    @Test
    void testUserToString() {
        String expected = "User{id='user123', username='testuser', email='test@example.com', " +
                "phone='1234567890', pwd='password123', totpsecret='totpsecret123'}";
        assertEquals(expected, testUser.toString());
    }
}
