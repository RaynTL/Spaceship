package com.example.spaceship.exception.datavalue;

/**
 * Exception thrown when a parameter has an invalid value.
 * This exception extends RuntimeException, indicating that it is an unchecked exception.
 */
public class InvalidValueException extends RuntimeException {

    /**
     * Constructs a new InvalidValueException with a specified invalid parameter value.
     * @param value The invalid parameter value that triggered the exception.
     */
    public InvalidValueException(String value) {
        super("Parameter: " + value + " has an invalid value");
    }
}
