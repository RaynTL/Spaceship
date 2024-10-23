package com.example.spaceship.repository;

import com.example.spaceship.model.Token;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for managing {@link Token} entities.
 * Extends {@link CrudRepository} to provide basic CRUD operations.
 */
public interface TokenRepository extends CrudRepository<Token, String> {

    /**
     * Finds a token by its JWT string.
     * @param jwtToken the JWT token string to search for
     * @return an Optional containing the found token, or empty if not found
     */
    Optional<Token> findByToken(String jwtToken);

    /**
     * Retrieves all tokens for a user that are neither valid nor revoked.
     * @param id the ID of the user whose tokens are to be retrieved
     * @return a list of tokens that are either expired or revoked
     */
    List<Token> findAllValidIsFalseOrRevokedIsFalseByUserId(Long id);
}
