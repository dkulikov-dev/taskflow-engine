package com.example.taskflow_engine.service;

import com.example.taskflow_engine.entity.Task;
import com.example.taskflow_engine.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class TaskExecutorService {

    private final TaskRepository taskRepository;
    // для теста без lombok


    @Scheduled(fixedDelay = 5000)
    public void executePendingTasks(){



        taskRepository.findByStatus("QUEUED").forEach(task -> {
            task.setStatus("RUNNING");
            taskRepository.save(task);

            try {
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


}