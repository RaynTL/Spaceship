package com.example.spaceship.dto;


import lombok.*;

/**
 * Data Transfer Object (DTO) for representing basic information of an incoming spaceship.
 * This class is used to transfer data between different layers of the application.
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Data
@Builder
public class IncomingSpaceshipBasicInfoDTO {

    /**
     * The name of the spaceship.
     */
    String name;

    /**
     * The platform the spaceship is associated with.
     */
    String platform;
}
