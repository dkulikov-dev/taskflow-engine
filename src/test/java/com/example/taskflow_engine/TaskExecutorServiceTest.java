package com.example.taskflow_engine;

import com.example.taskflow_engine.entity.Task;
import com.example.taskflow_engine.repository.TaskRepository;
import com.example.taskflow_engine.service.TaskExecutorService;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.mockito.Mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.List;

@ExtendWith(MockitoExtension.class)
class TaskExecutorServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    private TaskExecutorService taskExecutorService;

    @Captor
    private ArgumentCaptor<Task> taskCaptor;

    @Test
    void shouldCallSaveTwiceWhenProcessingTask(){

        Task task = new Task();
        when(taskRepository.findByStatus("QUEUED")).thenReturn(List.of(task));


        taskExecutorService.executePendingTasks();

        // проверяем, сколько раз вызван save
        verify(taskRepository, times(2)).save(any());
    }

    @Test
    void shouldNotSaveIfNoQueuedTasks() {

        when(taskRepository.findByStatus("QUEUED"))
                .thenReturn(List.of());

        taskExecutorService.executePendingTasks();


        verify(taskRepository, never()).save(any());
    }
}