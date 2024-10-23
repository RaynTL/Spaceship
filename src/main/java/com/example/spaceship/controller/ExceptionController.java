package com.example.spaceship.controller;

import com.example.spaceship.exception.ErrorMessage;
import com.example.spaceship.exception.datavalue.InvalidPageOrSizeException;
import com.example.spaceship.exception.datavalue.InvalidValueException;
import com.example.spaceship.exception.datavalue.MissingIncomingInfoException;
import com.example.spaceship.exception.spaceship.SpaceshipExistException;
import com.example.spaceship.exception.spaceship.SpaceshipNotFoundException;
import com.example.spaceship.exception.user.UserNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;

/**
 * Exception controller that handles custom exceptions within the application.
 * Uses the @RestControllerAdvice annotation to apply exception handling globally
 * across all controllers in the application.
 */
@RestControllerAdvice
public class ExceptionController {

    /**
     * Handles MissingIncomingInfoException.
     * @param missingIncomingInfo the thrown exception
     * @return ResponseEntity with the error message and HTTP status 400 (BAD_REQUEST)
     */
    @ExceptionHandler(MissingIncomingInfoException.class)
    ResponseEntity<ErrorMessage> missingIncomingInfoHandler(MissingIncomingInfoException missingIncomingInfo) {
        return new ResponseEntity<>(createErrorMessage(HttpStatus.BAD_REQUEST.value(), missingIncomingInfo.getMessage()), HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles InvalidValueException.
     * @param invalidValueException the thrown exception
     * @param webRequest the WebRequest associated with the request
     * @return ResponseEntity with the error message and HTTP status 400 (BAD_REQUEST)
     */
    @ExceptionHandler(InvalidValueException.class)
    ResponseEntity<ErrorMessage> invalidValueHandler(InvalidValueException invalidValueException, WebRequest webRequest) {
        return new ResponseEntity<>(createErrorMessage(HttpStatus.BAD_REQUEST.value(), invalidValueException.getMessage()), HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles SpaceshipNotFoundException.
     * @param spaceshipNotFoundException the thrown exception
     * @return ResponseEntity with the error message and HTTP status 404 (NOT_FOUND)
     */
    @ExceptionHandler(SpaceshipNotFoundException.class)
    ResponseEntity<ErrorMessage> spaceshipNotFoundHandler(SpaceshipNotFoundException spaceshipNotFoundException) {
        return new ResponseEntity<>(createErrorMessage(HttpStatus.NOT_FOUND.value(), spaceshipNotFoundException.getMessage()), HttpStatus.NOT_FOUND);
    }

    /**
     * Handles SpaceshipExistException.
     * @param spaceshipExistException the thrown exception
     * @return ResponseEntity with the error message and HTTP status 400 (BAD_REQUEST)
     */
    @ExceptionHandler(SpaceshipExistException.class)
    ResponseEntity<ErrorMessage> spaceshipExistHandler(SpaceshipExistException spaceshipExistException) {
        return new ResponseEntity<>(createErrorMessage(HttpStatus.BAD_REQUEST.value(), spaceshipExistException.getMessage()), HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles InvalidPageOrSizeException.
     * @param invalidPageOrSizeException the thrown exception
     * @return ResponseEntity with the error message and HTTP status 400 (BAD_REQUEST)
     */
    @ExceptionHandler(InvalidPageOrSizeException.class)
    ResponseEntity<ErrorMessage> invalidPageOrSizeHandler(InvalidPageOrSizeException invalidPageOrSizeException) {
        return new ResponseEntity<>(createErrorMessage(HttpStatus.BAD_REQUEST.value(), invalidPageOrSizeException.getMessage()), HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles UserNotFoundException and returns a standardized error response.
     * @param userNotFoundException the exception that was thrown
     * @return a ResponseEntity containing an ErrorMessage with details about the error
     */
    @ExceptionHandler(UserNotFoundException.class)
    ResponseEntity<ErrorMessage> userNotFoundHandler(UserNotFoundException userNotFoundException) {
        return new ResponseEntity<>(createErrorMessage(HttpStatus.NOT_FOUND.value(), userNotFoundException.getMessage()), HttpStatus.NOT_FOUND);
    }

    /**
     * Creates an ErrorMessage object with error details.
     * @param httpStatus the HTTP status code
     * @param message the error message
     * @return an ErrorMessage object containing the status, message, and timestamp
     */
    private ErrorMessage createErrorMessage(int httpStatus, String message) {
        return ErrorMessage.builder().status(httpStatus)
            .message(message)
            .timestamp(LocalDateTime.now()).build();
    }
}
