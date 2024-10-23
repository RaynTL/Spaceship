package com.example.spaceship.service.spaceship.impl;

import com.example.spaceship.dto.IncomingSpaceshipBasicInfoDTO;
import com.example.spaceship.exception.datavalue.InvalidPageOrSizeException;
import com.example.spaceship.exception.datavalue.InvalidValueException;
import com.example.spaceship.exception.datavalue.MissingIncomingInfoException;
import com.example.spaceship.exception.spaceship.SpaceshipExistException;
import com.example.spaceship.exception.spaceship.SpaceshipNotFoundException;
import com.example.spaceship.model.Spaceship;
import com.example.spaceship.repository.SpaceshipRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.PageRequest;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class SpaceshipServiceImplTest {

    @Mock
    private SpaceshipRepository spaceshipRepository;

    @InjectMocks
    private SpaceshipServiceImpl spaceshipService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testFindByIdThrowsInvalidValueExceptionWhenSpaceshipIdIsNull() {

        InvalidValueException exception = assertThrows(InvalidValueException.class, () -> {
            spaceshipService.findById(null);
        });

        assertEquals("Parameter: SPACESHIP has an invalid value", exception.getMessage());
    }

    @Test
    public void testFindByIdThrowsInvalidValueExceptionWhenSpaceshipIdIsEmpty() {

        String spaceshipId = "";

        InvalidValueException exception = assertThrows(InvalidValueException.class, () -> {
            spaceshipService.findById(spaceshipId);
        });

        assertEquals("Parameter: SPACESHIP has an invalid value", exception.getMessage());
    }

    @Test
    public void testFindByIdThrowsSpaceshipNotFoundException() {

        String spaceshipId = "noExistingId";

        Mockito.when(spaceshipRepository.findById(spaceshipId)).thenReturn(Optional.empty());

        SpaceshipNotFoundException exception = assertThrows(SpaceshipNotFoundException.class, () -> {
            spaceshipService.findById(spaceshipId);
        });

        assertEquals("Could not found spaceship: " + spaceshipId, exception.getMessage());
    }

    @Test
    public void testCreateSpaceshipThrowsMissingIncomingInfo() {

        assertThrows(MissingIncomingInfoException.class, () -> {
            spaceshipService.createSpaceship(null);
        });
    }

    @Test
    public void testCreateSpaceshipThrowsInvalidValueExceptionWhenNameIsNull() {

        IncomingSpaceshipBasicInfoDTO dto = IncomingSpaceshipBasicInfoDTO.builder().platform("MOVIE").build();

        assertThrows(InvalidValueException.class, () -> {
            spaceshipService.createSpaceship(dto);
        });
    }

    @Test
    public void testCreateSpaceshipThrowsInvalidValueExceptionWhenNameIsEmpty() {

        IncomingSpaceshipBasicInfoDTO dto = IncomingSpaceshipBasicInfoDTO.builder().name("")
            .platform("MOVIE").build();

        assertThrows(InvalidValueException.class, () -> {
            spaceshipService.createSpaceship(dto);
        });
    }

    @Test
    public void testCreateSpaceshipThrowsInvalidValueExceptionWhenPlatformIsNull() {

        IncomingSpaceshipBasicInfoDTO dto = IncomingSpaceshipBasicInfoDTO.builder().name("x-wing").build();

        assertThrows(InvalidValueException.class, () -> {
            spaceshipService.createSpaceship(dto);
        });
    }

    @Test
    public void testCreateSpaceshipThrowsInvalidValueExceptionWhenPlatformIsEmpty() {

        IncomingSpaceshipBasicInfoDTO dto = IncomingSpaceshipBasicInfoDTO.builder().name("x-wing")
            .platform("").build();

        assertThrows(InvalidValueException.class, () -> {
            spaceshipService.createSpaceship(dto);
        });
    }

    @Test
    public void testCreateSpaceshipThrowsInvalidValueExceptionWhenPlatformIsNotValid() {

        IncomingSpaceshipBasicInfoDTO dto = IncomingSpaceshipBasicInfoDTO.builder().name("x-wing")
            .platform("PELICULA").build();

        assertThrows(InvalidValueException.class, () -> {
            spaceshipService.createSpaceship(dto);
        });
    }

    @Test
    public void testCreateSpaceshipThrowsSpaceshipExistExceptionWhenSpaceshipAlreadyExists() {

        Spaceship spaceship = Spaceship.builder().name("x-wing")
            .platform("MOVIE").build();
        IncomingSpaceshipBasicInfoDTO dto = IncomingSpaceshipBasicInfoDTO.builder().name("x-wing")
            .platform("MOVIE").build();

        Mockito.when(spaceshipRepository.findByName(dto.getName())).thenReturn(Optional.of(spaceship));

        SpaceshipExistException exception = assertThrows(SpaceshipExistException.class, () -> {
            spaceshipService.createSpaceship(dto);
        });

        assertEquals("Spaceship name " + spaceship.getName() + " already exists", exception.getMessage());
    }

    @Test
    public void testCreateSpaceshipSavesNewSpaceshipWhenSpaceshipDoesNotExistPreviously() {

        IncomingSpaceshipBasicInfoDTO dto = IncomingSpaceshipBasicInfoDTO.builder().name("x-wing")
            .platform("MOVIE").build();

        Mockito.when(spaceshipRepository.findByName(dto.getName())).thenReturn(Optional.empty());

        spaceshipService.createSpaceship(dto);

        Mockito.verify(spaceshipRepository, Mockito.times(1)).save(Mockito.any(Spaceship.class));
    }

    @Test
    public void testUpdateSpaceshipThrowsMissingIncomingInfoExceptionWhenNameIsEmpty() {

        String spaceshipId = "existingId";
        IncomingSpaceshipBasicInfoDTO dto = null;

        assertThrows(MissingIncomingInfoException.class, () -> {
            spaceshipService.updateSpaceship(spaceshipId, dto);
        });
    }

    @Test
    public void testUpdateSpaceshipThrowsInvalidValueExceptionWhenNameIsEmpty() {

        String spaceshipId = "existingId";
        IncomingSpaceshipBasicInfoDTO dto = IncomingSpaceshipBasicInfoDTO.builder().name("")
            .platform("MOVIE").build();

        assertThrows(InvalidValueException.class, () -> {
            spaceshipService.updateSpaceship(spaceshipId, dto);
        });
    }

    @Test
    public void testUpdateSpaceshipThrowsInvalidValueExceptionWhenPlatformIsEmpty() {

        String spaceshipId = "existingId";
        IncomingSpaceshipBasicInfoDTO dto = IncomingSpaceshipBasicInfoDTO.builder().name("x-wing")
            .platform("").build();

        assertThrows(InvalidValueException.class, () -> {
            spaceshipService.updateSpaceship(spaceshipId, dto);
        });
    }

    @Test
    public void testUpdateSpaceshipThrowsSpaceshipNotFoundExceptionWhenSpaceshipDoesNotExist() {

        String spaceshipId = "nonExistentId";
        IncomingSpaceshipBasicInfoDTO dto = IncomingSpaceshipBasicInfoDTO.builder().name("x-wing")
            .platform("MOVIE").build();

        Mockito.when(spaceshipRepository.findById(spaceshipId)).thenReturn(Optional.empty());

        SpaceshipNotFoundException exception = assertThrows(SpaceshipNotFoundException.class, () -> {
            spaceshipService.updateSpaceship(spaceshipId, dto);
        });

        assertEquals("Could not found spaceship: " + spaceshipId, exception.getMessage());
    }

    @Test
    public void testUpdateSpaceshipUpdatesSpaceshipWhenSpaceshipExists() {

        String spaceshipId = "existingId";
        IncomingSpaceshipBasicInfoDTO dto = IncomingSpaceshipBasicInfoDTO.builder().name("x-wing")
            .platform("MOVIE").build();
        Spaceship existingSpaceship = Spaceship.builder().id("existingId").name("name")
            .platform("PLATFORM").build();

        Mockito.when(spaceshipRepository.findById(spaceshipId)).thenReturn(Optional.of(existingSpaceship));

        spaceshipService.updateSpaceship(spaceshipId, dto);

        assertEquals("x-wing", existingSpaceship.getName());
        assertEquals("MOVIE", existingSpaceship.getPlatform());

        Mockito.verify(spaceshipRepository, Mockito.times(1)).save(existingSpaceship);
    }

    @Test
    public void testDeleteSpaceshipByIdThrowsInvalidValueExceptionWhenSpaceshipIdIsEmpty() {

        String spaceshipId = "";

        assertThrows(InvalidValueException.class, () -> {
            spaceshipService.deleteSpaceshipById(spaceshipId);
        });
    }

    @Test
    public void testDeleteSpaceshipByIdThrowsSpaceshipNotFoundExceptionWhenSpaceshipDoesNotExist() {

        String spaceshipId = "nonExistentId";

        Mockito.when(spaceshipRepository.findById(spaceshipId)).thenReturn(Optional.empty());

        SpaceshipNotFoundException exception = assertThrows(SpaceshipNotFoundException.class, () -> {
            spaceshipService.deleteSpaceshipById(spaceshipId);
        });

        assertEquals("Could not found spaceship: " + spaceshipId, exception.getMessage());
    }

    @Test
    public void testDeleteSpaceshipByIdDeletesSpaceshipWhenSpaceshipExists() {

        String spaceshipId = "existingId";
        Spaceship existingSpaceship = new Spaceship();
        existingSpaceship.setId(spaceshipId);

        Mockito.when(spaceshipRepository.findById(spaceshipId)).thenReturn(Optional.of(existingSpaceship));

        spaceshipService.deleteSpaceshipById(spaceshipId);

        Mockito.verify(spaceshipRepository, Mockito.times(1)).delete(existingSpaceship);
    }


    @Test
    void testFindAllNegativePageThrowsInvalidPageOrSizeException() {

        int negativePage = -1;
        int size = 10;

        assertThrows(InvalidPageOrSizeException.class, () -> {
            spaceshipService.findAll(negativePage, size);
        });
    }

    @Test
    void testFindAllNegativeSizeThrowsInvalidPageOrSizeException() {

        int page = 0;
        int negativeSize = -1;

        assertThrows(InvalidPageOrSizeException.class, () -> {
            spaceshipService.findAll(page, negativeSize);
        });
    }

    @Test
    public void testFindAllReturnsListOfSpaceships() {

        int page = 0;
        int size = 5;
        List<Spaceship> expectedSpaceships = Arrays.asList(new Spaceship(), new Spaceship());

        Mockito.when(spaceshipRepository.findAll(PageRequest.of(page, size))).thenReturn(expectedSpaceships);

        List<Spaceship> actualSpaceships = spaceshipService.findAll(page, size);

        assertEquals(expectedSpaceships, actualSpaceships);
        Mockito.verify(spaceshipRepository, Mockito.times(1)).findAll(PageRequest.of(page, size));
    }

    @Test
    void testFindAllByContainingNameNegativePageThrowsInvalidPageOrSizeException() {

        int negativePage = -1;
        int size = 10;
        String spaceshipName = "wing";

        assertThrows(InvalidPageOrSizeException.class, () -> {
            spaceshipService.findAllByContainingName(spaceshipName, negativePage, size);
        });
    }

    @Test
    void testFindAllByContainingNameNegativeSizeThrowsInvalidPageOrSizeException() {

        int page = 0;
        int negativeSize = -1;
        String spaceshipName = "wing";

        assertThrows(InvalidPageOrSizeException.class, () -> {
            spaceshipService.findAllByContainingName(spaceshipName, page, negativeSize);
        });
    }

    @Test
    public void testFindAllByContainingNameReturnsListOfSpaceships() {

        int page = 0;
        int size = 5;
        String spaceshipName = "wing";
        List<Spaceship> expectedSpaceships = Arrays.asList(new Spaceship(), new Spaceship());

        Mockito.when(spaceshipRepository.findByNameContaining(spaceshipName, PageRequest.of(page, size))).thenReturn(expectedSpaceships);

        List<Spaceship> actualSpaceships = spaceshipService.findAllByContainingName(spaceshipName, page, size);

        assertEquals(expectedSpaceships, actualSpaceships);

        Mockito.verify(spaceshipRepository, Mockito.times(1)).findByNameContaining(spaceshipName, PageRequest.of(page, size));
    }
}