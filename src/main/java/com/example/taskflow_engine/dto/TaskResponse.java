package com.example.taskflow_engine.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record TaskResponse (
        UUID id,
        String taskType,
        String status,
        Object payload,
        Object result,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) { }