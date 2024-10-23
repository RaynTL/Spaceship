package com.example.spaceship.exception.spaceship;

/**
 * Exception thrown when attempting to create a spaceship that already exists.
 * This exception extends RuntimeException, indicating that it is an unchecked exception.
 */
public class SpaceshipExistException extends RuntimeException {

    /**
     * Constructs a new SpaceshipExistException with a specified spaceship name.
     * @param name the name of the spaceship that already exists.
     */
    public SpaceshipExistException(String name) {
        super(("Spaceship name " + name + " already exists"));
    }
}
