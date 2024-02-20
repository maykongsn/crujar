package com.spring.crujar.repository;

import com.spring.crujar.domain.Task;

import java.util.List;

public interface TaskRepository {
    List<Task> listAll();
}
