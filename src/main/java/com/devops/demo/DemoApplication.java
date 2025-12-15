package com.devops.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * DevOps CI/CD Demo Application
 * 
 * This application demonstrates a complete CI/CD pipeline using:
 * - Spring Boot for the REST API
 * - Maven for build automation
 * - Docker for containerization
 * - Kubernetes for orchestration
 * - Jenkins for automated deployment
 */
@SpringBootApplication
@EnableAsync
public class DemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);

        System.out.println("\n==================================================");
        System.out.println("DevOps Demo Application started successfully!");
        System.out.println("==================================================");
        System.out.println("Access health endpoint at: http://localhost:8080/health");
        System.out.println("Access app info at: http://localhost:8080/api/info");
        System.out.println("Access pipeline API at: http://localhost:8080/api/pipeline/status");
        System.out.println("==================================================\n");
    }

}
