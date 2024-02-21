package com.spring.crujar.service;


import com.spring.crujar.controller.request.TaskPostRequest;
import com.spring.crujar.controller.request.TaskPutRequest;
import com.spring.crujar.domain.Task;
import com.spring.crujar.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

@Service
@RequiredArgsConstructor
public class TaskService {
    private final TaskRepository taskRepository;

    public List<Task> listAll() {
        return taskRepository.findAll();
    }

    public Task findById(UUID id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Task not found!"));
    }

    public Task save(TaskPostRequest taskPostRequest) {
        return taskRepository.save(Task.builder().description(taskPostRequest.getDescription()).build());
    }

    public void replace(TaskPutRequest taskPutRequest) {
        Task taskSaved = findById(taskPutRequest.getId());
        Task task = Task.builder()
                .id(taskSaved.getId())
                .description(taskPutRequest.getDescription())
                .build();

        taskRepository.save(task);
    }

    public void delete(UUID id) {
        taskRepository.delete(findById(id));
    }
}
