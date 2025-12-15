package com.devops.demo.model;

/**
 * Model class representing application information
 * Used to return detailed app metadata via REST API
 */
public class AppInfo {
    
    private String applicationName;
    private String version;
    private String environment;
    private String timestamp;
    private String description;

    // Default constructor
    public AppInfo() {
    }

    // Parameterized constructor
    public AppInfo(String applicationName, String version, String environment, String timestamp, String description) {
        this.applicationName = applicationName;
        this.version = version;
        this.environment = environment;
        this.timestamp = timestamp;
        this.description = description;
    }

    // Getters and Setters
    public String getApplicationName() {
        return applicationName;
    }

    public void setApplicationName(String applicationName) {
        this.applicationName = applicationName;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getEnvironment() {
        return environment;
    }

    public void setEnvironment(String environment) {
        this.environment = environment;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "AppInfo{" +
                "applicationName='" + applicationName + '\'' +
                ", version='" + version + '\'' +
                ", environment='" + environment + '\'' +
                ", timestamp='" + timestamp + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
