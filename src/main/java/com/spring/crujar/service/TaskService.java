package com.spring.crujar.service;


import com.spring.crujar.controller.request.TaskPostRequest;
import com.spring.crujar.controller.request.TaskPutRequest;
import com.spring.crujar.domain.Task;
import com.spring.crujar.exception.BadRequestException;
import com.spring.crujar.mapper.TaskMapper;
import com.spring.crujar.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TaskService {
    private final TaskRepository taskRepository;

    public Page<Task> listAll(Pageable pageable) {
        return taskRepository.findAll(pageable);
    }

    public Task findById(UUID id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("Task not found!"));
    }

    public List<Task> findByTitle(String title) {
        return taskRepository.findByTitle(title);
    }

    @Transactional
    public Task save(TaskPostRequest taskPostRequest) {
        return taskRepository.save(TaskMapper.INSTANCE.toTask(taskPostRequest));
    }

    public void replace(TaskPutRequest taskPutRequest) {
        findById(taskPutRequest.getId());
        taskRepository.save(TaskMapper.INSTANCE.toTask(taskPutRequest));
    }

    public void delete(UUID id) {
        taskRepository.delete(findById(id));
    }
}
