package com.example.spaceship.controller.request;

/**
 * A record representing a request to log in a user.
 * This record encapsulates the necessary information required for user authentication.
 * @param email    The email address of the user, used to identify the account.
 * @param password The password for the user account, used for authentication.
 */
public record LoginRequest(
        String email,
        String password
) {
}
