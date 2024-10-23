package com.example.spaceship.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;


/**
 * Represents a spaceship entity within the application.
 * This class is mapped to the corresponding database table.
 */
@Entity
@Table
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Spaceship implements Serializable {

    /**
     * Unique identifier for the spaceship.
     */
    @Id
    private String id;

    /**
     * The name of the spaceship.
     */
    private String name;

    /**
     * The platform on which the spaceship operates.
     */
    private String platform;
}
