package com.example.spaceship.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Represents a user in the application.
 * This entity is mapped to the "usuario" table in the database.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "usuario")
public class User {

    /**
     * Unique identifier for the user.
     */
    @Id
    @GeneratedValue
    private Long id;

    /**
     * The name of the user.
     */
    private String name;

    /**
     * The email of the user, which must be unique.
     */
    @Column(unique = true)
    private String email;

    /**
     * The password of the user, stored in a hashed format.
     */
    private String password;

    /**
     * List of tokens associated with the user.
     * This relationship is defined as one-to-many, where a user can have multiple tokens.
     * Tokens are fetched lazily to improve performance.
     */
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<Token> tokenList;
}
