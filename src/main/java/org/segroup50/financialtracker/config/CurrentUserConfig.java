package org.segroup50.financialtracker.config;

import org.segroup50.financialtracker.data.model.User;

/**
 * A utility class for managing the currently logged-in user in the application.
 * This class maintains the user state in a static variable, providing thread-safe
 * access to the current user information throughout the application.
 */
public class CurrentUserConfig {
    // Static variable to store the current user
    private static User currentUser;

    /**
     * Retrieves the currently logged-in user.
     *
     * @return the current User object, or null if no user is logged in
     */
    public static User getCurrentUser() {
        return currentUser;
    }

    /**
     * Sets the currently logged-in user.
     *
     * @param user the User object to set as the current user
     * @throws IllegalArgumentException if the provided user is null
     */
    public static void setCurrentUser(User user) {
        currentUser = user;
    }

    /**
     * Clears the current user information, effectively logging out the user.
     */
    public static void clearCurrentUser() {
        currentUser = null;
    }

    /**
     * Checks whether there is a currently logged-in user.
     *
     * @return true if a user is currently logged in, false otherwise
     */
    public static boolean isUserLoggedIn() {
        return currentUser != null;
    }

    /**
     * Retrieves the ID of the currently logged-in user.
     *
     * @return the current user's ID, or null if no user is logged in
     */
    public static String getCurrentUserId() {
        return currentUser != null ? currentUser.getId() : null;
    }

    /**
     * Retrieves the username of the currently logged-in user.
     *
     * @return the current username, or null if no user is logged in
     */
    public static String getCurrentUsername() {
        return currentUser != null ? currentUser.getUsername() : null;
    }
}
