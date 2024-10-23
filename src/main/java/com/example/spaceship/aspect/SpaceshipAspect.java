package com.example.spaceship.aspect;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * An aspect that handles logging for the spaceship service.
 * This aspect uses Aspect-Oriented Programming (AOP) to intercept method calls
 * in the spaceship service package and perform additional actions, such as logging.
 */
@Aspect
@Component
public class SpaceshipAspect {

    private static final Logger logger = LoggerFactory.getLogger(SpaceshipAspect.class);

    /**
     * Logs a message if the provided spaceship ID is negative.
     * This advice is executed before any method in the spaceship service
     * that matches the signature `findById(..)` and receives a single argument.
     * @param spaceshipId The ID of the spaceship being searched for.
     *                    If this ID is negative, a log entry will be created.
     */
    @Before("execution(* com.example.spaceship.service.*.findById(..)) && args(spaceshipId)")
    public void logNegativeId(String spaceshipId) {
        if (spaceshipId != null && spaceshipId.startsWith("-")) {
            logger.info("Spaceship ID is negative: {}", spaceshipId);
        }
    }
}
