package com.example.spaceship.controller.response;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents a response containing access and refresh tokens.
 * This record is used to encapsulate the tokens returned to the client upon successful authentication
 * or token refresh operations.
 * @param accessToken The access token that is used for authenticating requests to protected resources.
 * @param refreshToken The refresh token that can be used to obtain a new access token when the current one expires.
 */
public record TokenResponse (
    @JsonProperty("access_token") String accessToken,
    @JsonProperty("refresh_token") String refreshToken ) {
}
