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
        // given
        Task task = new Task();
        when(taskRepository.findByStatus("QUEUED")).thenReturn(List.of(task));

        // when
        taskExecutorService.executePendingTasks();

        // then проверяем, сколько раз вызван save
        verify(taskRepository, times(3)).save(any());
    }

    @Test
    void shouldNotSaveIfNoQueuedTasks() {
        // given
        when(taskRepository.findByStatus("QUEUED"))
                .thenReturn(List.of());

        // when
        taskExecutorService.executePendingTasks();

        // then
        verify(taskRepository, never()).save(any());
    }
}