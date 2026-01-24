package com.example.taskflow_engine.service;

import com.example.taskflow_engine.dto.CreateTaskRequest;
import com.example.taskflow_engine.dto.TaskResponse;
import com.example.taskflow_engine.entity.Task;
import com.example.taskflow_engine.repository.TaskRepository;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.time.LocalDateTime;
import java.util.List;

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

    public List<TaskResponse> getAllTasks() {
        return taskRepository.findAll().stream()
                .map(task -> new TaskResponse(
                        task.getId(),
                        task.getTaskType(),
                        task.getStatus(),
                        task.getPayload(),
                        task.getResult(),
                        task.getCreatedAt(),
                        task.getUpdatedAt()
                ))
                .toList();
    }
}