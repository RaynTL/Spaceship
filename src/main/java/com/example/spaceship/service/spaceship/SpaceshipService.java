package com.example.spaceship.service.spaceship;

import com.example.spaceship.dto.IncomingSpaceshipBasicInfoDTO;
import com.example.spaceship.exception.datavalue.InvalidPageOrSizeException;
import com.example.spaceship.exception.datavalue.InvalidValueException;
import com.example.spaceship.exception.datavalue.MissingIncomingInfoException;
import com.example.spaceship.exception.spaceship.SpaceshipExistException;
import com.example.spaceship.exception.spaceship.SpaceshipNotFoundException;
import com.example.spaceship.model.Spaceship;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * Interface for managing spaceship operations.
 * Provides methods to perform CRUD operations and retrieve spaceship data.
 */
public interface SpaceshipService {

    /**
     * Retrieves a spaceship by its ID, caching the result.
     * @param spaceshipId the ID of the spaceship to retrieve.
     * @return the spaceship with the given ID.
     * @throws InvalidValueException if the spaceship ID is null or empty.
     * @throws SpaceshipNotFoundException if no spaceship is found with the given ID.
     */
    Spaceship findById(String spaceshipId);

    /**
     * Creates a new spaceship based on the provided data transfer object.
     * @param incomingSpaceshipBasicInfoDTO the DTO containing spaceship information.
     * @throws SpaceshipExistException if a spaceship with the same name already exists.
     * @throws MissingIncomingInfoException if the incoming DTO is null.
     * @throws InvalidValueException if the spaceship name or platform is invalid.
     */
    void createSpaceship(IncomingSpaceshipBasicInfoDTO incomingSpaceshipBasicInfoDTO);

    /**
     * Updates an existing spaceship with the provided data.
     * @param spaceshipId the ID of the spaceship to update.
     * @param incomingSpaceshipBasicInfoDTO the DTO containing updated spaceship information.
     * @throws SpaceshipNotFoundException if no spaceship is found with the given ID.
     * @throws MissingIncomingInfoException if the incoming DTO is null.
     * @throws InvalidValueException if the incoming DTO is invalid.
     */
    void updateSpaceship(String spaceshipId, IncomingSpaceshipBasicInfoDTO incomingSpaceshipBasicInfoDTO);

    /**
     * Deletes a spaceship by its ID.
     * @param spaceshipId the ID of the spaceship to delete.
     * @throws SpaceshipNotFoundException if no spaceship is found with the given ID.
     */
    void deleteSpaceshipById(String spaceshipId);

    /**
     * Retrieves a paginated list of all spaceships.
     * @param page the page number (zero-based).
     * @param size the number of spaceships per page.
     * @return a list of spaceships for the given page.
     * @throws InvalidPageOrSizeException if the page or size is negative.
     */
    List<Spaceship> findAll(int page, int size);

    /**
     * Retrieves a list of spaceships whose names contain the specified string.
     * @param page the page number (zero-based).
     * @param size the number of spaceships per page.
     * @param spaceshipName the substring to search for in spaceship names.
     * @return a list of matching spaceships.
     */
    List<Spaceship> findAllByContainingName(String spaceshipName, int page, int size);
}
