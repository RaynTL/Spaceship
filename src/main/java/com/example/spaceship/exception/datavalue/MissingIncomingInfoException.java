package com.example.spaceship.exception.datavalue;

/**
 * Exception thrown when required incoming information is missing.
 * This exception extends RuntimeException, indicating that it is an unchecked exception.
 */
public class MissingIncomingInfoException extends RuntimeException {

    /**
     * Constructs a new MissingIncomingInfoException with a default message.
     */
    public MissingIncomingInfoException() {
        super("Missing entering info");
    }
}
