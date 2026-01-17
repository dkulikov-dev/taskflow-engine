package com.example.taskflow_engine.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.Map;

public record CreateTaskRequest (
        @NotBlank String taskType,
        @NotNull Map<String, Object> payload)
{}