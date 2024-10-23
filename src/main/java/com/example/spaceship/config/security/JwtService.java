package com.example.spaceship.config.security;

import com.example.spaceship.model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.Map;

/**
 * Service for handling JSON Web Tokens (JWT).
 * This class provides methods to create, validate, and extract information from JWTs.
 */
@Service
public class JwtService {

    @Value("${application.security.jwt.secret-key}")
    private String secretKey;

    @Value("${application.security.jwt.expiration}")
    private long jwtExpiration;

    @Value("${application.security.jwt.refresh-token.expiration}")
    private long refreshExpiration;

    /**
     * Extracts the username (subject) from a JWT.
     * @param jwtToken the JWT token
     * @return the username contained in the token
     */
    public String extractUsername(String jwtToken) {
        return getClaim(jwtToken).getSubject();
    }

    /**
     * Generates a JWT for a user with a standard expiration time.
     * @param user the user for whom to generate the token
     * @return the generated JWT
     */
    public String generateToken(User user) {
        return buildToken(user, jwtExpiration);
    }

    /**
     * Generates a refresh token for a user.
     * @param user the user for whom to generate the refresh token
     * @return the generated refresh token
     */
    public String generateRefreshToken(User user) {
        return buildToken(user, refreshExpiration);
    }

    /**
     * Builds a JWT for a user with the specified expiration time.
     * @param user          the user for whom to build the token
     * @param jwtExpiration the expiration time for the token
     * @return the constructed JWT
     */
    private String buildToken(User user, long jwtExpiration) {

        return Jwts.builder()
            .id(user.getId().toString())
            .claims(Map.of("name", user.getName()))
            .subject(user.getEmail())
            .issuedAt(new Date(System.currentTimeMillis()))
            .expiration(new Date(System.currentTimeMillis() + jwtExpiration))
            .signWith(getSignInKey())
            .compact();
    }

    /**
     * Retrieves the signing key used for validating the JWT.
     * @return the SecretKey used for signing
     */
    private SecretKey getSignInKey() {

        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    /**
     * Validates a JWT against the provided user.
     * @param token the JWT to validate
     * @param user  the user to validate against
     * @return true if the token is valid, false otherwise
     */
    public boolean isTokenValid(String token, User user) {

        final String username = extractUsername(token);
        return (username.equals(user.getEmail())) && !isTokenExpired(token);
    }

    /**
     * Checks if a JWT has expired.
     * @param token the JWT to check
     * @return true if the token is expired, false otherwise
     */
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    /**
     * Extracts the expiration date from a JWT.
     * @param token the JWT to extract the expiration from
     * @return the expiration date
     */
    private Date extractExpiration(String token) {
        return getClaim(token).getExpiration();
    }

    private Claims getClaim(String token) {

        return Jwts.parser()
                .verifyWith(getSignInKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
