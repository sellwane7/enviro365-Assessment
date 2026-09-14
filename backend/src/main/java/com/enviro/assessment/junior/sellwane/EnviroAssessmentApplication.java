package com.enviro.assessment.junior.sellwane;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * This is the entry point of the whole application.
 * When you press "Run" in IntelliJ on this class, Spring Boot:
 *   1. Starts an embedded web server (Tomcat) on port 8080
 *   2. Scans this package (and sub-packages) for @Component, @Service,
 *      @Repository, @RestController classes and wires them together
 *   3. Creates the H2 in-memory database and the tables for our entities
 */
@SpringBootApplication
public class EnviroAssessmentApplication {

    public static void main(String[] args) {
        SpringApplication.run(EnviroAssessmentApplication.class, args);
    }
}
