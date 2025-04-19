package org.segroup50.financialtracker.data.dao;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.segroup50.financialtracker.data.model.User;

import java.io.File;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UserDaoTest {
    private UserDao userDao;
    private static final String TEST_CSV_FILE = "test_users.csv";
    private static final String CSV_HEADER = "id,username,email,phone,pwd,totpsecret";

    @BeforeEach
    void setUp() {
        // Use a test CSV file instead of the production one
        userDao = new UserDao() {
            @Override
            protected void initCsvFile() {
                this.csvFile = TEST_CSV_FILE;
                this.csvHeader = CSV_HEADER;
                super.initCsvFile();
            }
        };
    }

    @AfterEach
    void tearDown() {
        // Clean up the test file after each test
        File file = new File(TEST_CSV_FILE);
        if (file.exists()) {
            file.delete();
        }
    }

    @Test
    void testAddAndGetUser() {
        User user = new User(null, "testuser", "test@example.com", "1234567890", "password", "secret");
        assertTrue(userDao.addUser(user));

        List<User> users = userDao.getAllUsers();
        assertEquals(1, users.size());

        User retrieved = users.get(0);
        assertNotNull(retrieved.getId());
        assertEquals("testuser", retrieved.getUsername());
        assertEquals("test@example.com", retrieved.getEmail());
    }

    @Test
    void testGetUserById() {
        User user = new User(null, "testuser", "test@example.com", "1234567890", "password", "secret");
        userDao.addUser(user);

        List<User> users = userDao.getAllUsers();
        String id = users.get(0).getId();

        User retrieved = userDao.getUserById(id);
        assertNotNull(retrieved);
        assertEquals(id, retrieved.getId());
    }

    @Test
    void testGetUserByUsername() {
        User user = new User(null, "testuser", "test@example.com", "1234567890", "password", "secret");
        userDao.addUser(user);

        User retrieved = userDao.getUserByUsername("testuser");
        assertNotNull(retrieved);
        assertEquals("testuser", retrieved.getUsername());
    }

    @Test
    void testUpdateUser() {
        User user = new User(null, "testuser", "test@example.com", "1234567890", "password", "secret");
        userDao.addUser(user);

        List<User> users = userDao.getAllUsers();
        User toUpdate = users.get(0);
        toUpdate.setEmail("updated@example.com");

        assertTrue(userDao.updateUser(toUpdate));

        User updated = userDao.getUserById(toUpdate.getId());
        assertEquals("updated@example.com", updated.getEmail());
    }

    @Test
    void testDeleteUser() {
        User user = new User(null, "testuser", "test@example.com", "1234567890", "password", "secret");
        userDao.addUser(user);

        List<User> users = userDao.getAllUsers();
        String id = users.get(0).getId();

        assertTrue(userDao.deleteUser(id));
        assertEquals(0, userDao.getAllUsers().size());
        assertNull(userDao.getUserById(id));
    }

    @Test
    void testAddMultipleUsers() {
        User user1 = new User(null, "user1", "user1@example.com", "1111111111", "pass1", "secret1");
        User user2 = new User(null, "user2", "user2@example.com", "2222222222", "pass2", "secret2");

        assertTrue(userDao.addUser(user1));
        assertTrue(userDao.addUser(user2));

        List<User> users = userDao.getAllUsers();
        assertEquals(2, users.size());
    }

    @Test
    void testGetNonExistentUser() {
        assertNull(userDao.getUserById("nonexistent"));
        assertNull(userDao.getUserByUsername("nonexistent"));
    }

    @Test
    void testUpdateNonExistentUser() {
        User user = new User("nonexistent", "testuser", "test@example.com", "1234567890", "password", "secret");
        assertFalse(userDao.updateUser(user));
    }

    @Test
    void testDeleteNonExistentUser() {
        assertFalse(userDao.deleteUser("nonexistent"));
    }

    @Test
    void testFromCsvLineWithInvalidData() {
        assertNull(userDao.fromCsvLine("invalid,data"));
    }
}
