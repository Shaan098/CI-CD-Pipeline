package com.devops.demo.controller;

import com.devops.demo.model.AppInfo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * REST Controller for health checks and application information
 * Used by Kubernetes liveness and readiness probes
 */
@RestController
public class HealthController {

    @Value("${app.version:1.0.0}")
    private String appVersion;

    @Value("${app.environment:development}")
    private String environment;

    /**
     * Simple health check endpoint
     * Used by Kubernetes liveness probe
     * 
     * @return HTTP 200 with status message
     */
    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> health() {
        Map<String, String> response = new HashMap<>();
        response.put("status", "UP");
        response.put("timestamp", LocalDateTime.now().toString());
        return ResponseEntity.ok(response);
    }

    /**
     * Application information endpoint
     * Returns detailed information about the application
     * 
     * @return Application information including version, environment, and uptime
     */
    @GetMapping("/api/info")
    public ResponseEntity<AppInfo> getInfo() {
        AppInfo info = new AppInfo();
        info.setApplicationName("DevOps CI/CD Demo");
        info.setVersion(appVersion);
        info.setEnvironment(environment);
        info.setTimestamp(LocalDateTime.now().toString());
        info.setDescription("Java application with Jenkins CI/CD pipeline, Docker containerization, and Kubernetes orchestration");
        return ResponseEntity.ok(info);
    }

    /**
     * Welcome endpoint
     * 
     * @return Welcome message
     */
    @GetMapping("/")
    public ResponseEntity<Map<String, Object>> welcome() {
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Welcome to DevOps CI/CD Demo!");
        response.put("version", appVersion);
        response.put("environment", environment);
        response.put("endpoints", new String[]{
            "/health - Health check",
            "/api/info - Application information"
        });
        return ResponseEntity.ok(response);
    }
}
