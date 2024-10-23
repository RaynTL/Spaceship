package com.example.spaceship.controller;

import com.example.spaceship.controller.request.LoginRequest;
import com.example.spaceship.controller.request.RegisterRequest;
import com.example.spaceship.controller.response.TokenResponse;
import com.example.spaceship.service.spaceship.auth.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controller for authentication-related operations, including user registration,
 * login, and token refreshing.
 */
@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    AuthService authService;

    /**
     * Handles user registration requests.
     * @param registerRequest the registration details provided in the request body
     * @return ResponseEntity containing the generated token response with HTTP status 200 (OK)
     */
    @PostMapping("/register")
    public ResponseEntity<TokenResponse> authenticate(@RequestBody final RegisterRequest registerRequest) {

        final TokenResponse tokenResponse = authService.register(registerRequest);
        return ResponseEntity.ok(tokenResponse);
    }

    /**
     * Handles user login requests.
     * @param loginRequest the login credentials provided in the request body
     * @return ResponseEntity containing the generated token response with HTTP status 200 (OK)
     */
    @PostMapping("/login")
    public ResponseEntity<TokenResponse> authenticate(@RequestBody final LoginRequest loginRequest) {

        final TokenResponse tokenResponse = authService.login(loginRequest);
        return ResponseEntity.ok(tokenResponse);
    }


    /**
     * Handles token refresh requests.
     * @param authHeader the Authorization header containing the token to refresh
     * @return ResponseEntity containing the new token response with HTTP status 200 (OK)
     */
    @PostMapping("/refresh")
    public ResponseEntity<TokenResponse> authenticate(@RequestHeader(HttpHeaders.AUTHORIZATION) final String authHeader) {

        final TokenResponse tokenResponse = authService.refreshToken(authHeader);
        return ResponseEntity.ok(tokenResponse);
    }
}
