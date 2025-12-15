package com.devops.demo;

import com.devops.demo.controller.HealthController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Unit tests for HealthController
 * Tests all REST endpoints for proper functionality
 */
@WebMvcTest(HealthController.class)
public class HealthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    /**
     * Test health endpoint returns 200 OK
     */
    @Test
    public void testHealthEndpoint() throws Exception {
        mockMvc.perform(get("/health"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("UP"))
                .andExpect(jsonPath("$.timestamp").exists());
    }

    /**
     * Test info endpoint returns application information
     */
    @Test
    public void testInfoEndpoint() throws Exception {
        mockMvc.perform(get("/api/info"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.applicationName").value("DevOps CI/CD Demo"))
                .andExpect(jsonPath("$.version").exists())
                .andExpect(jsonPath("$.environment").exists())
                .andExpect(jsonPath("$.timestamp").exists());
    }

    /**
     * Test welcome endpoint returns proper message
     */
    @Test
    public void testWelcomeEndpoint() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Welcome to DevOps CI/CD Demo!"))
                .andExpect(jsonPath("$.endpoints").isArray());
    }
}
