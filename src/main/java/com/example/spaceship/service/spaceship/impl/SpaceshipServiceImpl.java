package com.example.spaceship.service.spaceship.impl;

import com.example.spaceship.dto.IncomingSpaceshipBasicInfoDTO;
import com.example.spaceship.exception.datavalue.InvalidPageOrSizeException;
import com.example.spaceship.exception.datavalue.InvalidValueException;
import com.example.spaceship.exception.datavalue.MissingIncomingInfoException;
import com.example.spaceship.exception.spaceship.SpaceshipExistException;
import com.example.spaceship.exception.spaceship.SpaceshipNotFoundException;
import com.example.spaceship.model.Spaceship;
import com.example.spaceship.repository.SpaceshipRepository;
import com.example.spaceship.service.spaceship.SpaceshipService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Implementation of the SpaceshipService interface, providing methods
 * for managing spaceships, including CRUD operations and data validation.
 */
@Service
public class SpaceshipServiceImpl implements SpaceshipService {

    private static final Logger logger = LoggerFactory.getLogger(SpaceshipServiceImpl.class);

    @Autowired
    SpaceshipRepository spaceshipRepository;

    /**
     * Retrieves a spaceship by its ID, caching the result.
     * @param spaceshipId the ID of the spaceship to retrieve.
     * @return the spaceship with the given ID.
     * @throws InvalidValueException if the spaceship ID is null or empty.
     * @throws SpaceshipNotFoundException if no spaceship is found with the given ID.
     */
    @Override
    @Cacheable(value = "spaceship", key = "#spaceshipId")
    public Spaceship findById(String spaceshipId) {
        logger.info("Fetching spaceship from database with ID: {}", spaceshipId);

        if (spaceshipId == null || spaceshipId.isEmpty()) {
            throw new InvalidValueException("SPACESHIP");
        }

        Optional<Spaceship> optionalSpaceship = spaceshipRepository.findById(spaceshipId);

        if (optionalSpaceship.isEmpty()) {
            throw new SpaceshipNotFoundException(spaceshipId);
        }

        return optionalSpaceship.get();
    }

    /**
     * Creates a new spaceship based on the provided data transfer object.
     * @param incomingSpaceshipBasicInfoDTO the DTO containing spaceship information.
     * @throws SpaceshipExistException if a spaceship with the same name already exists.
     * @throws MissingIncomingInfoException if the incoming DTO is null.
     * @throws InvalidValueException if the spaceship name or platform is invalid.
     */
    @Override
    public void createSpaceship(IncomingSpaceshipBasicInfoDTO incomingSpaceshipBasicInfoDTO) {

        checkBasicSpaceshipIncomingData(incomingSpaceshipBasicInfoDTO);

        Optional<Spaceship> existSpaceship = spaceshipRepository.findByName(incomingSpaceshipBasicInfoDTO.getName());

        if (existSpaceship.isPresent()) {
            throw new SpaceshipExistException(incomingSpaceshipBasicInfoDTO.getName());
        }

        Spaceship spaceship = Spaceship.builder()
            .id(UUID.randomUUID().toString())
            .name(incomingSpaceshipBasicInfoDTO.getName())
            .platform(incomingSpaceshipBasicInfoDTO.getPlatform())
            .build();

        spaceshipRepository.save(spaceship);
    }

    /**
     * Updates an existing spaceship with the provided data.
     * @param spaceshipId the ID of the spaceship to update.
     * @param incomingSpaceshipBasicInfoDTO the DTO containing updated spaceship information.
     * @throws SpaceshipNotFoundException if no spaceship is found with the given ID.
     * @throws MissingIncomingInfoException if the incoming DTO is null.
     * @throws InvalidValueException if the incoming DTO is invalid.
     */
    @Override
    public void updateSpaceship(String spaceshipId, IncomingSpaceshipBasicInfoDTO incomingSpaceshipBasicInfoDTO) {

        checkBasicSpaceshipIncomingData(incomingSpaceshipBasicInfoDTO);

        Spaceship spaceship = findById(spaceshipId);

        spaceship.setName(incomingSpaceshipBasicInfoDTO.getName());
        spaceship.setPlatform(incomingSpaceshipBasicInfoDTO.getPlatform().toUpperCase());

        spaceshipRepository.save(spaceship);
    }

    /**
     * Deletes a spaceship by its ID.
     * @param spaceshipId the ID of the spaceship to delete.
     * @throws SpaceshipNotFoundException if no spaceship is found with the given ID.
     */
    @Override
    @CacheEvict(value = "spaceship", key = "#spaceshipId")
    public void deleteSpaceshipById(String spaceshipId) {

        Spaceship spaceship = findById(spaceshipId);

        spaceshipRepository.delete(spaceship);
    }

    /**
     * Retrieves a paginated list of all spaceships.
     * @param page the page number (zero-based).
     * @param size the number of spaceships per page.
     * @return a list of spaceships for the given page.
     * @throws InvalidPageOrSizeException if the page or size is negative.
     */
    @Override
    public List<Spaceship> findAll(int page, int size) {

        if (page < 0 || size < 0) {
            throw new InvalidPageOrSizeException();
        }

        return spaceshipRepository.findAll(PageRequest.of(page, size));
    }

    /**
     * Retrieves a list of spaceships whose names contain the specified string.
     * @param spaceshipName the substring to search for in spaceship names.
     * @return a list of matching spaceships.
     */
    @Override
    public List<Spaceship> findAllByContainingName(String spaceshipName, int page, int size) {

        if (page < 0 || size < 0) {
            throw new InvalidPageOrSizeException();
        }

        return spaceshipRepository.findByNameContaining(spaceshipName, PageRequest.of(page, size));
    }

    /**
     * Validates the incoming spaceship data.
     * @param incomingSpaceshipBasicInfoDTO the DTO containing spaceship information.
     * @throws MissingIncomingInfoException if the DTO is null.
     * @throws InvalidValueException if the name or platform is invalid.
     */
    private void checkBasicSpaceshipIncomingData(IncomingSpaceshipBasicInfoDTO incomingSpaceshipBasicInfoDTO) {

        if (incomingSpaceshipBasicInfoDTO == null) {
            throw new MissingIncomingInfoException();
        }

        if (incomingSpaceshipBasicInfoDTO.getName() == null ||
            incomingSpaceshipBasicInfoDTO.getName().isEmpty()) {

            throw new InvalidValueException("Spaceship NAME");
        }

        if (incomingSpaceshipBasicInfoDTO.getPlatform() == null ||
            incomingSpaceshipBasicInfoDTO.getPlatform().isEmpty()) {

            throw new InvalidValueException("PLATFORM");
        }

        if (!isValidPlatform(incomingSpaceshipBasicInfoDTO.getPlatform())) {
            throw new InvalidValueException("PLATFORM");
        }
    }

    /**
     * Checks if the provided platform is valid.
     * @param platform the platform to validate.
     * @return true if the platform is valid, false otherwise.
     */
    private boolean isValidPlatform(String platform) {
        return platform.equalsIgnoreCase("SERIES") ||
            platform.equalsIgnoreCase("MOVIE");
    }
}
