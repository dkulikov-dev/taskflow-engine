package com.example.taskflow_engine.service;

/*import com.example.taskflow_engine.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class TaskExecutorService {

    private static final Logger log = LoggerFactory.getLogger(TaskExecutorService.class);

    private final TaskRepository taskRepository;
    // для теста без lombok


    @Scheduled(fixedDelay = 5000)
    public void executePendingTasks(){



        taskRepository.findByStatus("QUEUED").forEach(task -> {
            task.setStatus("RUNNING");
            taskRepository.save(task);

            try {
                // Имитация длительной обработки задачи (в реальном проекте заменить на асинхронную работу)
                Thread.sleep(2000);
                task.setStatus("COMPLETED");
            }catch (InterruptedException e){
                task.setStatus("FAILED");
            }finally {
                task.setUpdatedAt(LocalDateTime.now());
                taskRepository.save(task);
            }

            task.setStatus("COMPLETED");
            task.setUpdatedAt(LocalDateTime.now());
            taskRepository.save(task);
        });
    }

}*/

import com.example.taskflow_engine.entity.Task;
import com.example.taskflow_engine.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskExecutorService {

    private static final Logger log = LoggerFactory.getLogger(TaskExecutorService.class);

    private final TaskRepository taskRepository;

    @Scheduled(fixedDelay = 5000)
    public void executePendingTasks() {
        log.debug("Checking for QUEUED tasks...");
        List<Task> queuedTasks = taskRepository.findByStatus("QUEUED");
        if (queuedTasks.isEmpty()) {
            return;
        }

        for (Task task : queuedTasks) {
            processTask(task);
        }
    }

    @Transactional
    protected void processTask(Task task) {
        try {
            log.info("Starting execution of task id={}", task.getId());
            task.setStatus("RUNNING");
            task.setUpdatedAt(LocalDateTime.now());
            taskRepository.save(task);

            // Имитация работы
            Thread.sleep(2000);

            task.setStatus("COMPLETED");
            task.setResult("{\"success\": true}");
            task.setUpdatedAt(LocalDateTime.now());
            taskRepository.save(task);

            log.info("Task id={} completed successfully", task.getId());

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt(); // восстанавливаем статус прерывания
            handleTaskFailure(task, "Execution interrupted");
        } catch (Exception e) {
            handleTaskFailure(task, "Unexpected error: " + e.getMessage());
            log.error("Unexpected error during task execution id={}", task.getId(), e);
        }
    }

    private void handleTaskFailure(Task task, String reason) {
        log.warn("Task id={} failed: {}", task.getId(), reason);
        task.setStatus("FAILED");
        task.setResult("{\"error\": \"" + reason + "\"}");
        task.setUpdatedAt(LocalDateTime.now());
        taskRepository.save(task);
    }
}