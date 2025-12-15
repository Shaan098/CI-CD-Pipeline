package com.devops.demo.service;

import com.devops.demo.model.PipelineExecution;
import com.devops.demo.model.PipelineStage;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class PipelineService {

    private final ConcurrentHashMap<String, PipelineExecution> executions = new ConcurrentHashMap<>();
    private PipelineExecution currentExecution = null;
    private final List<PipelineExecution> executionHistory = new ArrayList<>();

    public PipelineExecution getCurrentExecution() {
        return currentExecution;
    }

    public List<PipelineExecution> getExecutionHistory() {
        return new ArrayList<>(executionHistory);
    }

    public PipelineExecution triggerPipeline() {
        if (currentExecution != null && "RUNNING".equals(currentExecution.getStatus())) {
            throw new IllegalStateException("Pipeline is already running");
        }

        String executionId = UUID.randomUUID().toString().substring(0, 8);
        PipelineExecution execution = new PipelineExecution(executionId);
        execution.setStatus("RUNNING");
        execution.setStartTime(LocalDateTime.now());

        currentExecution = execution;
        executions.put(executionId, execution);

        // Execute pipeline asynchronously
        executePipelineAsync(execution);

        return execution;
    }

    @Async
    public void executePipelineAsync(PipelineExecution execution) {
        try {
            long startTime = System.currentTimeMillis();

            // Execute each stage
            for (PipelineStage stage : PipelineStage.values()) {
                execution.setCurrentStage(stage);
                executeStage(execution, stage);
            }

            // Pipeline completed successfully
            execution.setStatus("SUCCESS");
            execution.setEndTime(LocalDateTime.now());
            execution.setDurationMs(System.currentTimeMillis() - startTime);

            // Add to history
            executionHistory.add(0, execution);
            if (executionHistory.size() > 10) {
                executionHistory.remove(executionHistory.size() - 1);
            }

        } catch (Exception e) {
            execution.setStatus("FAILED");
            execution.setEndTime(LocalDateTime.now());
            executionHistory.add(0, execution);
        }
    }

    private void executeStage(PipelineExecution execution, PipelineStage stage) throws InterruptedException {
        // Mark stage as running
        execution.updateStageStatus(stage, "running", 0);

        // Simulate stage execution with progress updates
        int duration = getStageDuration(stage);
        int steps = 10;
        int stepDuration = duration / steps;

        for (int i = 1; i <= steps; i++) {
            Thread.sleep(stepDuration);
            int progress = (i * 100) / steps;
            execution.updateStageStatus(stage, "running", progress);
        }

        // Mark stage as complete
        execution.completeStage(stage);
    }

    private int getStageDuration(PipelineStage stage) {
        // Realistic durations for each stage (in milliseconds)
        switch (stage) {
            case SOURCE_CONTROL:
                return 2000; // 2 seconds
            case BUILD_MAVEN:
                return 5000; // 5 seconds
            case DOCKER_BUILD:
                return 4000; // 4 seconds
            case PUSH_REGISTRY:
                return 3000; // 3 seconds
            case DEPLOY_KUBERNETES:
                return 4000; // 4 seconds
            default:
                return 3000;
        }
    }
}
