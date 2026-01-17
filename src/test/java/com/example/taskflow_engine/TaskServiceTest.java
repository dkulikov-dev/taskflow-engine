package com.example.taskflow_engine;

import com.example.taskflow_engine.service.TaskService;
import com.example.taskflow_engine.dto.CreateTaskRequest;
import com.example.taskflow_engine.dto.TaskResponse;
import com.example.taskflow_engine.entity.Task;
import com.example.taskflow_engine.repository.TaskRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    private TaskService taskService;
    @BeforeEach
    void setUp() {
        taskService = new TaskService(taskRepository, new ObjectMapper());
    }

    @Test
    void shouldCreateTask() {
        // given
        CreateTaskRequest request = new CreateTaskRequest("SEND_EMAIL", Map.of("to", "test@example.com"));

        Task savedTask = Task.builder()
                .id(UUID.randomUUID())
                .taskType("SEND_EMAIL")
                .payload("{\"to\":\"test@example.com\"}")
                .status("QUEUED")
                .build();
        when(taskRepository.save(any())).thenReturn(savedTask);

        // when
        TaskResponse response = taskService.createTask(request);

        // then
        assertThat(response.taskType()).isEqualTo("SEND_EMAIL");
        assertThat(response.status()).isEqualTo("QUEUED");
    }
}