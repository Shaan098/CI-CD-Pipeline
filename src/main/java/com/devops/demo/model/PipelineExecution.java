package com.devops.demo.model;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class PipelineExecution {
    private String executionId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private PipelineStage currentStage;
    private String status; // "RUNNING", "SUCCESS", "FAILED", "PENDING"
    private Map<PipelineStage, StageStatus> stageStatuses;
    private long durationMs;

    public PipelineExecution() {
        this.stageStatuses = new HashMap<>();
        this.status = "PENDING";
        // Initialize all stages as pending
        for (PipelineStage stage : PipelineStage.values()) {
            stageStatuses.put(stage, new StageStatus("pending", 0));
        }
    }

    public PipelineExecution(String executionId) {
        this();
        this.executionId = executionId;
        this.startTime = LocalDateTime.now();
    }

    // Getters and Setters
    public String getExecutionId() {
        return executionId;
    }

    public void setExecutionId(String executionId) {
        this.executionId = executionId;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public PipelineStage getCurrentStage() {
        return currentStage;
    }

    public void setCurrentStage(PipelineStage currentStage) {
        this.currentStage = currentStage;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Map<PipelineStage, StageStatus> getStageStatuses() {
        return stageStatuses;
    }

    public void setStageStatuses(Map<PipelineStage, StageStatus> stageStatuses) {
        this.stageStatuses = stageStatuses;
    }

    public long getDurationMs() {
        return durationMs;
    }

    public void setDurationMs(long durationMs) {
        this.durationMs = durationMs;
    }

    // Helper methods
    public void updateStageStatus(PipelineStage stage, String status, int progress) {
        stageStatuses.put(stage, new StageStatus(status, progress));
    }

    public void completeStage(PipelineStage stage) {
        stageStatuses.put(stage, new StageStatus("complete", 100));
    }

    public void failStage(PipelineStage stage) {
        stageStatuses.put(stage, new StageStatus("failed", 0));
    }

    // Inner class for stage status
    public static class StageStatus {
        private String status; // "pending", "running", "complete", "failed"
        private int progress; // 0-100

        public StageStatus() {
        }

        public StageStatus(String status, int progress) {
            this.status = status;
            this.progress = progress;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public int getProgress() {
            return progress;
        }

        public void setProgress(int progress) {
            this.progress = progress;
        }
    }
}
