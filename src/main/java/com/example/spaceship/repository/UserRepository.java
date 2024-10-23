package com.example.spaceship.repository;

import com.example.spaceship.model.User;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

/**
 * Repository interface for managing {@link User} entities.
 * Extends {@link CrudRepository} to provide basic CRUD operations.
 */
public interface UserRepository extends CrudRepository<User, String> {

    /**
     * Finds a user by their email address.
     * @param email the email address of the user to search for
     * @return an Optional containing the found user, or empty if no user is found with the given email
     */
    Optional<User> findByEmail(String email);
}
