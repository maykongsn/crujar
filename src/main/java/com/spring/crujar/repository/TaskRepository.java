package com.spring.crujar.repository;

import com.spring.crujar.domain.Task;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface TaskRepository extends JpaRepository<Task, UUID> {
    List<Task> findByTitle(String title);
}
