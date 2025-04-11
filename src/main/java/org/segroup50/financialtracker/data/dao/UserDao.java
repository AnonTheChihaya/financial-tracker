package org.segroup50.financialtracker.data.dao;

import org.segroup50.financialtracker.data.BaseCsvDao;
import org.segroup50.financialtracker.data.model.User;

import java.util.List;
import java.util.UUID;

/**
 * Data Access Object for managing User entities in CSV format.
 * Provides CRUD operations for User objects stored in a CSV file.
 */
public class UserDao extends BaseCsvDao<User> {
    private static final String CSV_FILE = "users.csv";
    private static final String CSV_HEADER = "id,username,email,phone,pwd,totpsecret";

    /**
     * Constructs a new UserDao instance with default CSV file and header.
     */
    public UserDao() {
        super(CSV_FILE, CSV_HEADER);
    }

    @Override
    protected String getId(User user) {
        return user.getId();
    }

    @Override
    protected String toCsvLine(User user) {
        // Generate a new UUID if the user doesn't have an ID
        if (user.getId() == null || user.getId().isEmpty()) {
            user.setId(UUID.randomUUID().toString());
        }
        return String.join(",",
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getPhone(),
                user.getPwd(),
                user.getTotpsecret());
    }

    @Override
    protected User fromCsvLine(String csvLine) {
        String[] parts = csvLine.split(",");
        // Skip malformed lines
        if (parts.length != 6) {
            return null;
        }
        return new User(parts[0], parts[1], parts[2], parts[3], parts[4], parts[5]);
    }

    /**
     * Retrieves a user by their unique identifier.
     * 
     * @param id The unique identifier of the user
     * @return The User object if found, null otherwise
     */
    public User getUserById(String id) {
        return getAll().stream()
                .filter(user -> user.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    /**
     * Retrieves a user by their username.
     * 
     * @param username The username to search for
     * @return The User object if found, null otherwise
     */
    public User getUserByUsername(String username) {
        return getAll().stream()
                .filter(user -> user.getUsername().equals(username))
                .findFirst()
                .orElse(null);
    }

    /**
     * Updates an existing user in the data store.
     * 
     * @param updatedUser The User object with updated information
     * @return true if the update was successful, false otherwise
     */
    public boolean updateUser(User updatedUser) {
        return update(updatedUser, updatedUser.getId());
    }

    /**
     * Deletes a user from the data store.
     * 
     * @param id The unique identifier of the user to delete
     * @return true if the deletion was successful, false otherwise
     */
    public boolean deleteUser(String id) {
        return delete(id);
    }

    /**
     * Adds a new user to the data store.
     * 
     * @param user The User object to add
     * @return true if the addition was successful, false otherwise
     */
    public boolean addUser(User user) {
        return add(user);
    }

    /**
     * Retrieves all users from the data store.
     * 
     * @return A list of all User objects
     */
    public List<User> getAllUsers() {
        return getAll();
    }
}
