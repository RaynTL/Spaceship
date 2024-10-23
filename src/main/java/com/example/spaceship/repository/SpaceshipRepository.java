package com.example.spaceship.repository;

import com.example.spaceship.model.Spaceship;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for managing {@link Spaceship} entities.
 * Extends {@link CrudRepository} to provide basic CRUD operations
 * and additional methods for querying spaceships based on various criteria.
 */
@Repository
public interface SpaceshipRepository extends CrudRepository<Spaceship, String> {

    /**
     * Retrieves a spaceship by its ID.
     * @param spaceshipId the ID of the spaceship to retrieve.
     * @return an {@link Optional} containing the found spaceship, or empty if no spaceship is found with the given ID.
     */
    Optional<Spaceship> findById(String spaceshipId);

    /**
     * Retrieves a spaceship by its name.
     * @param spaceshipName the name of the spaceship to retrieve.
     * @return an {@link Optional} containing the found spaceship, or empty if no spaceship is found with the given name.
     */
    Optional<Spaceship> findByName(String spaceshipName);

    /**
     * Retrieves a paginated list of all spaceships.
     * @param pageable the pagination information (page number, size).
     * @return a list of {@link Spaceship} entities for the specified page.
     */
    List<Spaceship> findAll(Pageable pageable);

    /**
     * Retrieves a list of spaceships whose names contain the specified substring.
     * @param spaceshipName the substring to search for in spaceship names.
     * @param pageable the pagination information (page number, size).
     * @return a list of {@link Spaceship} entities whose names contain the given substring.
     */
    List<Spaceship> findByNameContaining(String spaceshipName, Pageable pageable);
}
