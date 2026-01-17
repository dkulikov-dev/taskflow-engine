package com.example.taskflow_engine.service;

import com.example.taskflow_engine.dto.CreateTaskRequest;
import com.example.taskflow_engine.dto.TaskResponse;
import com.example.taskflow_engine.entity.Task;
import com.example.taskflow_engine.repository.TaskRepository;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor

public class TaskService {
    private final TaskRepository taskRepository;
    private final ObjectMapper objectMapper;


    public TaskResponse createTask (CreateTaskRequest request) {

        try {
            String payloadJson = objectMapper.writeValueAsString(request.payload());

            Task task = Task.builder()
                    .taskType(request.taskType())
                    .payload(payloadJson)
                    .status("QUEUED")
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .build();

            task = taskRepository.save(task);

            return new TaskResponse(
                    task.getId(),
                    task.getTaskType(),
                    task.getStatus(),
                    task.getPayload(),
                    task.getResult(),
                    task.getCreatedAt(),
                    task.getUpdatedAt()
            );

        }catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to serialize payload", e);
        }
    }
}