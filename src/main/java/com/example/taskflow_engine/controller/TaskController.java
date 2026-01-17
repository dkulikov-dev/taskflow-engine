package com.example.taskflow_engine.controller;

import com.example.taskflow_engine.service.TaskService;
import com.example.taskflow_engine.dto.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("api/tasks")
@RequiredArgsConstructor
public class TaskController {
    private TaskService taskService;

    @PostMapping
    public ResponseEntity <TaskResponse> createTask (@Valid @RequestBody CreateTaskRequest request){
        TaskResponse response = taskService.createTask(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }




    /*
    @GetMapping("/{id}")
    public ResponseEntity<TaskResponse> getTask(@PathVariable UUID id) {
        TaskResponse response = taskService.getTask(id);
        return ResponseEntity.ok(response);
    }*/

}