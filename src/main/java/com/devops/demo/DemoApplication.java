package com.devops.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main Spring Boot Application for DevOps CI/CD Demo
 * 
 * This application demonstrates a complete CI/CD pipeline using:
 * - Jenkins for automation
 * - Docker for containerization
 * - Kubernetes for orchestration
 */
@SpringBootApplication
public class DemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
        System.out.println("DevOps Demo Application started successfully!");
        System.out.println("Access health endpoint at: http://localhost:8080/health");
        System.out.println("Access app info at: http://localhost:8080/api/info");
    }
}
