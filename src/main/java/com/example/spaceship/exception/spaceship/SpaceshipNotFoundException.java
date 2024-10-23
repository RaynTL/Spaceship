package com.example.spaceship.exception.spaceship;

/**
 * Exception thrown when a requested spaceship is not found.
 * This exception extends RuntimeException, indicating that it is an unchecked exception.
 */
public class SpaceshipNotFoundException extends RuntimeException{

    /**
     * Constructs a new SpaceshipNotFoundException with a specified spaceship ID.
     * @param spaceshipId the ID of the spaceship that could not be found.
     */
    public SpaceshipNotFoundException(String spaceshipId) {
        super("Could not found spaceship: " + spaceshipId);
    }
}
