package com.example.spaceship.exception;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * Represents an error message structure used for API error responses.
 * This class is used to encapsulate details about an error that occurred during API processing.
 * It provides information such as the HTTP status code, a descriptive error message, and a timestamp
 * indicating when the error occurred.
 */
@Data
@Builder
public class ErrorMessage {

    /**
     * The HTTP status code associated with the error.
     * This indicates the type of error that occurred (e.g., 404 for not found, 400 for bad request).
     */
    private int status;

    /**
     * A descriptive message providing more details about the error.
     * This message is intended to help the client understand what went wrong.
     */
    private String message;

    /**
     * The timestamp indicating when the error occurred.
     * This is formatted as a string to provide a clear and standard representation of the date and time.
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd hh:mm:s")
    private LocalDateTime timestamp;
}
