package com.example.spaceship.service.spaceship.auth.impl;

import com.example.spaceship.config.security.JwtService;
import com.example.spaceship.controller.request.LoginRequest;
import com.example.spaceship.controller.request.RegisterRequest;
import com.example.spaceship.controller.response.TokenResponse;
import com.example.spaceship.exception.user.UserNotFoundException;
import com.example.spaceship.model.Token;
import com.example.spaceship.model.User;
import com.example.spaceship.repository.TokenRepository;
import com.example.spaceship.repository.UserRepository;
import com.example.spaceship.service.spaceship.auth.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Implementation of the AuthService interface for handling user authentication and token management.
 */
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    /**
     * Registers a new user and generates authentication tokens.
     * @param registerRequest the registration details
     * @return TokenResponse containing access and refresh tokens
     */
    public TokenResponse register(RegisterRequest registerRequest) {

        User user = User.builder()
                .name(registerRequest.name())
                .email(registerRequest.email())
                .password(passwordEncoder.encode(registerRequest.password()))
                .build();
        User savedUser = userRepository.save(user);
        String jwtToken = jwtService.generateToken(user);
        String refreshToken =  jwtService.generateRefreshToken(user);
        saveUserToken(savedUser, jwtToken);
        return new TokenResponse(jwtToken, refreshToken);
    }

    /**
     * Authenticates a user and generates authentication tokens.
     * @param loginRequest the login credentials
     * @return TokenResponse containing access and refresh tokens
     */
    public TokenResponse login(LoginRequest loginRequest) {

        authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.email(),
                        loginRequest.password()));

        User user = userRepository.findByEmail(loginRequest.email()).orElseThrow();
        String jwtToken = jwtService.generateToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);

        revokeAllUserTokens(user);
        saveUserToken(user, jwtToken);

        return new TokenResponse(jwtToken, refreshToken);
    }

    /**
     * Refreshes the access token using the provided refresh token.
     * @param authHeader the Authorization header containing the refresh token
     * @return TokenResponse containing a new access token and the original refresh token
     */
    public TokenResponse refreshToken(String authHeader) {

        if (authHeader == null || !authHeader.startsWith("Bearer")) {
            throw new IllegalArgumentException("Invalid Bearer token");
        }

        final String refreshToken = authHeader.substring(7);
        final String userEmail = jwtService.extractUsername(refreshToken);

        if (userEmail == null) {
            throw new IllegalArgumentException("Invalid Refresh token");
        }

        final User user = userRepository.findByEmail(userEmail).orElseThrow(() -> new UserNotFoundException(userEmail));

        if (!jwtService.isTokenValid(refreshToken, user)) {
            throw new IllegalArgumentException("Invalid Refresh token");
        }

        final String accessToken = jwtService.generateToken(user);
        revokeAllUserTokens(user);
        saveUserToken(user, accessToken);

        return new TokenResponse(accessToken, refreshToken);
    }

    /**
     * Saves the user's JWT token in the repository.
     * @param user the user for whom the token is being saved
     * @param jwtToken the token to save
     */
    private void saveUserToken(User user, String jwtToken) {

        Token token = Token.builder()
                .user(user)
                .token(jwtToken)
                .tokenType(Token.TokenType.BEARER)
                .expired(false)
                .revoked(false)
                .build();

        tokenRepository.save(token);
    }

    /**
     * Revokes all tokens associated with a user.
     * @param user the user whose tokens are to be revoked
     */
    private void revokeAllUserTokens(User user) {

        final List<Token> validUserTokenList = tokenRepository.findAllValidIsFalseOrRevokedIsFalseByUserId(user.getId());

        if (!validUserTokenList.isEmpty()) {
            for (final Token token : validUserTokenList) {
                token.setExpired(true);
                token.setRevoked(true);
            }
        }

        tokenRepository.saveAll(validUserTokenList);
    }
}
