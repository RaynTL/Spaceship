package com.example.spaceship.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents an authentication token associated with a user.
 * This entity is managed by JPA and is mapped to the corresponding database table.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Token {


    /**
     * Enum representing the type of token.
     */
    public enum TokenType {
        BEARER
    }

    /**
     * Unique identifier for the token.
     */
    @Id
    @GeneratedValue
    public Long id;

    /**
     * The actual token string, which must be unique.
     */
    @Column(unique = true)
    public String token;

    /**
     * The type of token, defaults to BEARER.
     */
    @Enumerated(EnumType.STRING)
    public TokenType tokenType = TokenType.BEARER;

    /**
     * Indicates whether the token has been revoked.
     */
    public boolean revoked;

    /**
     * Indicates whether the token has expired.
     */
    public boolean expired;

    /**
     * The user associated with this token.
     * This relationship is defined as many-to-one, where multiple tokens can be linked to a single user.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    public User user;
}
