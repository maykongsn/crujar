package com.spring.crujar.service;


import com.spring.crujar.controller.request.TaskPostRequest;
import com.spring.crujar.controller.request.TaskPutRequest;
import com.spring.crujar.domain.Task;
import com.spring.crujar.mapper.TaskMapper;
import com.spring.crujar.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TaskService {
    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;

    public List<Task> listAll() {
        return taskRepository.findAll();
    }

    public Task findById(UUID id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Task not found!"));
    }

    public Task save(TaskPostRequest taskPostRequest) {
        return taskRepository.save(taskMapper.toTask(taskPostRequest));
    }

    public void replace(TaskPutRequest taskPutRequest) {
        findById(taskPutRequest.getId());
        taskRepository.save(taskMapper.toTask(taskPutRequest));
    }

    public void delete(UUID id) {
        taskRepository.delete(findById(id));
    }
}
