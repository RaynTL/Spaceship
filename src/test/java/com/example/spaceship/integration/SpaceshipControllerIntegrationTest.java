package com.example.spaceship.integration;

import com.example.spaceship.config.security.JwtService;
import com.example.spaceship.controller.SpaceshipController;
import com.example.spaceship.dto.IncomingSpaceshipBasicInfoDTO;
import com.example.spaceship.exception.datavalue.InvalidPageOrSizeException;
import com.example.spaceship.exception.datavalue.InvalidValueException;
import com.example.spaceship.exception.datavalue.MissingIncomingInfoException;
import com.example.spaceship.exception.spaceship.SpaceshipExistException;
import com.example.spaceship.exception.spaceship.SpaceshipNotFoundException;
import com.example.spaceship.model.Spaceship;
import com.example.spaceship.repository.TokenRepository;
import com.example.spaceship.repository.UserRepository;
import com.example.spaceship.service.spaceship.SpaceshipService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class SpaceshipControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private JwtService jwtService;

    @MockBean
    private TokenRepository tokenRepository;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private SpaceshipService spaceshipService;

    @InjectMocks
    SpaceshipController spaceshipController;

    @WithMockUser
    @Test
    public void testGetTestData() throws Exception {

        mockMvc.perform(get("/spaceship/test")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("Test"));
    }

    @WithMockUser
    @Test
    public void testFindByIdInvalidId() throws Exception {

        when(spaceshipService.findById(anyString())).thenThrow(new InvalidValueException("SPACESHIP"));

        mockMvc.perform(get("/spaceship/{id}", "-1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @WithMockUser
    @Test
    public void testFindByIdNotFound() throws Exception {

        when(spaceshipService.findById(anyString())).thenThrow(new SpaceshipNotFoundException("1"));

        mockMvc.perform(get("/spaceship/{id}", "1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @WithMockUser
    @Test
    public void testFindByIdSuccess() throws Exception {

        Spaceship spaceship = new Spaceship();
        spaceship.setId("1");
        spaceship.setName("Nave de Prueba");
        spaceship.setPlatform("Plataforma 1");

        when(spaceshipService.findById("1")).thenReturn(spaceship);

        mockMvc.perform(get("/spaceship/{id}", "1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.name").value("Nave de Prueba"))
                .andExpect(jsonPath("$.platform").value("Plataforma 1"));
    }

    @WithMockUser
    @Test
    public void testCreateSpaceshipMissingInfo() throws Exception {

        doThrow(new MissingIncomingInfoException())
                .when(spaceshipService).createSpaceship(any(IncomingSpaceshipBasicInfoDTO.class));

        mockMvc.perform(post("/spaceship")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest());
    }

    @WithMockUser
    @Test
    public void testCreateSpaceshipInvalidNameValue() throws Exception {

        doThrow(new InvalidValueException("Spaceship NAME"))
                .when(spaceshipService).createSpaceship(any(IncomingSpaceshipBasicInfoDTO.class));

        mockMvc.perform(post("/spaceship")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\": \"\", \"platform\": \"Plataforma 1\"}"))
                .andExpect(status().isBadRequest());
    }

    @WithMockUser
    @Test
    public void testCreateSpaceshipInvalidPlatformValue() throws Exception {

        doThrow(new InvalidValueException("Spaceship NAME"))
                .when(spaceshipService).createSpaceship(any(IncomingSpaceshipBasicInfoDTO.class));

        mockMvc.perform(post("/spaceship")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\": \"name\", \"platform\": \"\"}"))
                .andExpect(status().isBadRequest());
    }

    @WithMockUser
    @Test
    public void testCreateSpaceshipSpaceshipExists() throws Exception {

        doThrow(new SpaceshipExistException("Nave de Prueba"))
                .when(spaceshipService).createSpaceship(any(IncomingSpaceshipBasicInfoDTO.class));

        mockMvc.perform(post("/spaceship")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\": \"Nave de Prueba\", \"platform\": \"Plataforma 1\"}"))
                .andExpect(status().isBadRequest());
    }

    @WithMockUser
    @Test
    public void testCreateSpaceshipSuccess() throws Exception {

        IncomingSpaceshipBasicInfoDTO incomingSpaceshipBasicInfoDTO = IncomingSpaceshipBasicInfoDTO.builder()
                .name("Nave de Prueba")
                .platform("Plataforma 1")
                .build();

        mockMvc.perform(post("/spaceship")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\": \"Nave de Prueba\", \"platform\": \"Plataforma 1\"}"))
                .andExpect(status().isCreated());

        verify(spaceshipService).createSpaceship(incomingSpaceshipBasicInfoDTO);
    }

    @WithMockUser
    @Test
    public void testUpdateSpaceshipMissingInfo() throws Exception {

        doThrow(new MissingIncomingInfoException())
                .when(spaceshipService).updateSpaceship(anyString(), any(IncomingSpaceshipBasicInfoDTO.class));

        mockMvc.perform(put("/spaceship/{id}", "1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}")) // Enviar un cuerpo vacío
                .andExpect(status().isBadRequest());
    }

    @WithMockUser
    @Test
    public void testUpdateSpaceshipInvalidValue() throws Exception {

        IncomingSpaceshipBasicInfoDTO incomingSpaceshipBasicInfoDTO = IncomingSpaceshipBasicInfoDTO.builder()
                .name("")
                .platform("Updated Platform")
                .build();

        doThrow(new InvalidValueException("SPACESHIP"))
                .when(spaceshipService).updateSpaceship("1", incomingSpaceshipBasicInfoDTO);

        mockMvc.perform(put("/spaceship/{id}", "1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\": \"\", \"platform\": \"Updated Platform\"}"))
                .andExpect(status().isBadRequest());
    }

    @WithMockUser
    @Test
    public void testUpdateSpaceshipNotFound() throws Exception {

        IncomingSpaceshipBasicInfoDTO incomingSpaceshipBasicInfoDTO = IncomingSpaceshipBasicInfoDTO.builder()
                .name("Updated Ship")
                .platform("Updated Platform")
                .build();

        doThrow(new SpaceshipNotFoundException("1"))
                .when(spaceshipService).updateSpaceship("1", incomingSpaceshipBasicInfoDTO);

        mockMvc.perform(put("/spaceship/{id}", "1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\": \"Updated Ship\", \"platform\": \"Updated Platform\"}"))
                .andExpect(status().isNotFound());
    }

    @WithMockUser
    @Test
    public void testUpdateSpaceshipSuccess() throws Exception {

        IncomingSpaceshipBasicInfoDTO incomingSpaceshipBasicInfoDTO = IncomingSpaceshipBasicInfoDTO.builder()
                .name("Updated Ship")
                .platform("Updated Platform")
                .build();

        mockMvc.perform(put("/spaceship/{id}", "1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\": \"Updated Ship\", \"platform\": \"Updated Platform\"}"))
                .andExpect(status().isOk());

        verify(spaceshipService).updateSpaceship("1", incomingSpaceshipBasicInfoDTO);
    }

    @WithMockUser
    @Test
    public void testDeleteSpaceshipInvalidValue() throws Exception {

        doThrow(new InvalidValueException("SPACESHIP"))
                .when(spaceshipService).deleteSpaceshipById(anyString());

        mockMvc.perform(delete("/spaceship/{id}", "-1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @WithMockUser
    @Test
    public void testDeleteSpaceshipNotFound() throws Exception {

        doThrow(new SpaceshipNotFoundException("1"))
                .when(spaceshipService).deleteSpaceshipById(anyString());

        mockMvc.perform(delete("/spaceship/{id}", "1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @WithMockUser
    @Test
    public void testDeleteSpaceshipSuccess() throws Exception {

        mockMvc.perform(delete("/spaceship/{id}", "1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        spaceshipService.deleteSpaceshipById("1");
    }

    @WithMockUser
    @Test
    public void testFindAllInvalidPageSize() throws Exception {

        when(spaceshipService.findAll(anyInt(), anyInt())).thenThrow(new InvalidPageOrSizeException());

        mockMvc.perform(get("/spaceship/list")
                        .param("page", "-1")
                        .param("size", "10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @WithMockUser
    @Test
    public void testFindAllSuccess() throws Exception {

        List<Spaceship> spaceships = Arrays.asList(new Spaceship(), new Spaceship());

        when(spaceshipService.findAll(0, 10)).thenReturn(spaceships);

        mockMvc.perform(get("/spaceship/list")
                        .param("page", "0")
                        .param("size", "10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @WithMockUser
    @Test
    public void testFindAllByContainingNameInvalidPageSize() throws Exception {

        when(spaceshipService.findAllByContainingName(anyString(), anyInt(), anyInt()))
                .thenThrow(new InvalidPageOrSizeException());

        mockMvc.perform(get("/spaceship/name/{name}", "Nave")
                        .param("page", "-1")
                        .param("size", "10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @WithMockUser
    @Test
    public void testFindAllByContainingNameSuccess() throws Exception {

        List<Spaceship> spaceships = Arrays.asList(new Spaceship(), new Spaceship());

        when(spaceshipService.findAllByContainingName("Nave", 0, 10)).thenReturn(spaceships);

        mockMvc.perform(get("/spaceship/name/{name}", "Nave")
                        .param("page", "0")
                        .param("size", "10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2));
    }
}
