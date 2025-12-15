package com.devops.demo.controller;

import com.devops.demo.model.PipelineExecution;
import com.devops.demo.service.PipelineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/pipeline")
@CrossOrigin(origins = "*")
public class PipelineController {

    @Autowired
    private PipelineService pipelineService;

    @GetMapping("/status")
    public ResponseEntity<Map<String, Object>> getPipelineStatus() {
        PipelineExecution currentExecution = pipelineService.getCurrentExecution();

        Map<String, Object> response = new HashMap<>();

        if (currentExecution != null) {
            response.put("executionId", currentExecution.getExecutionId());
            response.put("status", currentExecution.getStatus());
            response.put("currentStage", currentExecution.getCurrentStage());
            response.put("startTime", currentExecution.getStartTime());
            response.put("endTime", currentExecution.getEndTime());
            response.put("durationMs", currentExecution.getDurationMs());
            response.put("stages", currentExecution.getStageStatuses());
        } else {
            response.put("status", "IDLE");
            response.put("message", "No pipeline execution is currently running");
        }

        return ResponseEntity.ok(response);
    }

    @PostMapping("/trigger")
    public ResponseEntity<Map<String, Object>> triggerPipeline() {
        try {
            PipelineExecution execution = pipelineService.triggerPipeline();

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Pipeline triggered successfully");
            response.put("executionId", execution.getExecutionId());
            response.put("startTime", execution.getStartTime());

            return ResponseEntity.ok(response);
        } catch (IllegalStateException e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
        }
    }

    @GetMapping("/history")
    public ResponseEntity<List<PipelineExecution>> getPipelineHistory() {
        List<PipelineExecution> history = pipelineService.getExecutionHistory();
        return ResponseEntity.ok(history);
    }

    @GetMapping("/stages")
    public ResponseEntity<Map<String, Object>> getStages() {
        PipelineExecution currentExecution = pipelineService.getCurrentExecution();

        Map<String, Object> response = new HashMap<>();

        if (currentExecution != null) {
            response.put("stages", currentExecution.getStageStatuses());
            response.put("currentStage", currentExecution.getCurrentStage());
        } else {
            response.put("message", "No active pipeline execution");
        }

        return ResponseEntity.ok(response);
    }
}
