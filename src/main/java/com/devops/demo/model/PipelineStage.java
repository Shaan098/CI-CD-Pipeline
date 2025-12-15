package com.devops.demo.model;

public enum PipelineStage {
    SOURCE_CONTROL("Source Control", "Code repository on GitHub with webhook integration"),
    BUILD_MAVEN("Build with Maven", "Compile Java source code and run unit tests"),
    DOCKER_BUILD("Docker Containerization", "Build optimized Docker image with multi-stage build"),
    PUSH_REGISTRY("Push to Registry", "Upload container image to Docker Hub"),
    DEPLOY_KUBERNETES("Deploy to Kubernetes", "Rolling update deployment with zero downtime");

    private final String displayName;
    private final String description;

    PipelineStage(String displayName, String description) {
        this.displayName = displayName;
        this.description = description;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getDescription() {
        return description;
    }
}
