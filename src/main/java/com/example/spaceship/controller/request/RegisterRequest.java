package com.example.spaceship.controller.request;

/**
 * A record representing a request to register a new user.
 * This record encapsulates the necessary information required for user registration.
 * @param email    The email address of the user. This value must be unique.
 * @param password The password for the user account. This value should be securely hashed before storage.
 * @param name     The name of the user. This value can be used for personalizing user experiences.
 */
public record RegisterRequest(
    String email,
    String password,
    String name
) {
}
