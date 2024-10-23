package com.example.spaceship.exception.datavalue;

/**
 * Exception thrown when the pagination parameters PAGE and SIZE are not positive.
 * This exception extends RuntimeException, indicating that it is an unchecked exception.
 */
public class InvalidPageOrSizeException extends RuntimeException {

    /**
     * Constructs a new InvalidPageOrSizeException with a default error message.
     */
    public InvalidPageOrSizeException() {
        super("PAGE and SIZE value must be positive");
    }
}
