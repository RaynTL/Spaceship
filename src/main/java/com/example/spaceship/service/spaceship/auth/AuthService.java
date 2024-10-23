package com.example.spaceship.service.spaceship.auth;

import com.example.spaceship.controller.request.LoginRequest;
import com.example.spaceship.controller.request.RegisterRequest;
import com.example.spaceship.controller.response.TokenResponse;

/**
 * Interface defining authentication services for user registration,
 * login, and token management.
 */
public interface AuthService {

    /**
     * Registers a new user and generates authentication tokens.
     * @param registerRequest the registration details provided by the user
     * @return TokenResponse containing the generated access and refresh tokens
     */
    TokenResponse register(RegisterRequest registerRequest);

    /**
     * Authenticates a user based on provided login credentials.
     * @param loginRequest the login credentials submitted by the user
     * @return TokenResponse containing the generated access and refresh tokens
     */
    TokenResponse login(LoginRequest loginRequest);

    /**
     * Refreshes the access token using a valid refresh token.
     * @param authHeader the Authorization header containing the refresh token
     * @return TokenResponse containing the new access token and the original refresh token
     */
    TokenResponse refreshToken(String authHeader);
}
