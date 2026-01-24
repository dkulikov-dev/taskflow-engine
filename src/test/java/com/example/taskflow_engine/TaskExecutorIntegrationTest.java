package com.example.taskflow_engine;

import com.example.taskflow_engine.entity.Task;
import com.example.taskflow_engine.repository.TaskRepository;
import com.example.taskflow_engine.service.TaskExecutorService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDateTime;
import java.util.UUID;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;

@SpringBootTest
@Testcontainers
            class TaskExecutorIntegrationTest {


    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16.0")
            .withDatabaseName("taskflow")
            .withUsername("taskflow")
            .withPassword("taskflow123");

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private TaskExecutorService taskExecutorService;

    @Autowired
    private ObjectMapper objectMapper;


    /**
     * Динамически подставляет URL, username и password от Testcontainers в Spring Boot.
     * Без этого Spring будет использовать application.yml с localhost:5432, что приведёт к ошибке.
     */
    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @Test
    void shouldTransitionFromQueuedToRunningToCompleted() {

        Task task = Task.builder()
                .taskType("TEST")
                .status("QUEUED")
                .payload("{}")
                .result("{}")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        task = taskRepository.save(task);
        UUID id = task.getId();

        // Прямой вызов вместо ожидания @Scheduled — для быстроты и предсказуемости теста.
        taskExecutorService.executePendingTasks();

        await().atMost(3, SECONDS)
                .untilAsserted(() -> {
                    Task result = taskRepository.findById(id).orElseThrow();
                    assertThat(result.getStatus()).isEqualTo("COMPLETED");
                });
    }
}