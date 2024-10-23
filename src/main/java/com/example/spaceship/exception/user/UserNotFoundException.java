package com.example.spaceship.exception.user;

/**
 * Exception thrown when a user is not found in the system.
 * This exception extends RuntimeException, indicating that it is an unchecked exception.
 */
public class UserNotFoundException extends RuntimeException {

    /**
     * Constructs a new UserNotFoundException with a detailed message.
     * @param username the username of the user that was not found
     */
    public UserNotFoundException(String username) {
        super("User: " + username + " not found");
    }
}
