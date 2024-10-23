package com.example.spaceship.controller;

import com.example.spaceship.dto.IncomingSpaceshipBasicInfoDTO;
import com.example.spaceship.model.Spaceship;
import com.example.spaceship.service.spaceship.SpaceshipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for managing spaceship operations.
 * Provides endpoints for creating, updating, deleting, and retrieving spaceships.
 */
@RestController
@RequestMapping("/spaceship")
public class SpaceshipController {

    @Autowired
    private SpaceshipService spaceshipService;


    /**
     * Test endpoint to verify that the service is functioning.
     * @return A test message "Test".
     */
    @GetMapping("/test")
    public String getTestData() {
        return "Test";
    }

    /**
     * Retrieves a spaceship by its ID.
     * @param id The ID of the spaceship to retrieve.
     * @return The spaceship corresponding to the provided ID.
     */
    @GetMapping("/{id}")
    public Spaceship findById(@PathVariable String id) {
        return spaceshipService.findById(id);
    }

    /**
     * Creates a new spaceship.
     * @param incomingSpaceshipBasicInfoDTO Object containing the basic information of the new spaceship.
     */
    @PostMapping()
    public ResponseEntity<Void> createSpaceship(@RequestBody IncomingSpaceshipBasicInfoDTO incomingSpaceshipBasicInfoDTO) {
        spaceshipService.createSpaceship(incomingSpaceshipBasicInfoDTO);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    /**
     * Updates an existing spaceship.
     * @param id The ID of the spaceship to update.
     * @param incomingSpaceshipBasicInfoDTO Object containing the new information for the spaceship.
     */
    @PutMapping("/{id}")
    public void updateSpaceShip(@PathVariable String id,
        @RequestBody IncomingSpaceshipBasicInfoDTO incomingSpaceshipBasicInfoDTO) {

        spaceshipService.updateSpaceship(id, incomingSpaceshipBasicInfoDTO);
    }

    /**
     * Deletes a spaceship by its ID.
     * @param id The ID of the spaceship to delete.
     */
    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable String id) {
        spaceshipService.deleteSpaceshipById(id);
    }

    /**
     * Retrieves a paginated list of all spaceships.
     * @param page The page number to retrieve (zero-indexed).
     * @param size The number of elements per page.
     * @return A list of spaceships on the requested page.
     */
    @GetMapping("/list")
    public List<Spaceship> findAll(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        return spaceshipService.findAll(page, size);
    }

    /**
     * Searches for spaceships whose names contain the provided string.
     * @param name The string to search for in spaceship names.
     * @param page The page number to retrieve (zero-indexed).
     * @param size The number of elements per page.
     * @return A list of spaceships that contain the provided name.
     */
    @GetMapping("/name/{name}")
    public List<Spaceship> findAllByContainingName(@PathVariable String name,
        @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {

        return spaceshipService.findAllByContainingName(name, page, size);
    }
}
