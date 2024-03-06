package com.spring.crujar.unit.repository;

import com.spring.crujar.domain.Task;
import com.spring.crujar.repository.TaskRepository;
import com.spring.crujar.util.TaskCreator;
import lombok.extern.log4j.Log4j2;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import javax.validation.*;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@DataJpaTest
@DisplayName("Tests for Task Repository")
@Log4j2
class TaskRepositoryTest {
    @Autowired
    private TaskRepository taskRepository;

    @Test
    @DisplayName("Should be able to save a task")
    void saveTaskTest() {
        Task task = TaskCreator.createTaskToBeSaved();

        Task taskSaved = this.taskRepository.save(task);

        Assertions.assertThat(taskSaved).isNotNull();
        Assertions.assertThat(taskSaved.getId()).isNotNull();
        Assertions.assertThat(taskSaved.getTitle()).isEqualTo(task.getTitle());
    }

    @Test
    @DisplayName("Should be able to update a task")
    void updateTaskTest() {
        Task task = TaskCreator.createTaskToBeSaved();

        Task taskSaved = this.taskRepository.save(task);

        taskSaved.setTitle("New title");

        Task taskUpdated = this.taskRepository.save(taskSaved);

        Assertions.assertThat(taskUpdated).isNotNull();
        Assertions.assertThat(taskUpdated.getId()).isNotNull();
        Assertions.assertThat(taskUpdated.getTitle()).isEqualTo(taskSaved.getTitle());
    }

    @Test
    @DisplayName("Should be able to delete a task")
    void deleteTaskTest() {
        Task task = TaskCreator.createTaskToBeSaved();

        Task taskSaved = this.taskRepository.save(task);

        this.taskRepository.delete(taskSaved);

        Optional<Task> taskOptional = this.taskRepository.findById(taskSaved.getId());

        Assertions.assertThat(taskOptional).isEmpty();
    }

    @Test
    @DisplayName("Should be able to find a task by title")
    void findByTitleTest() {
        Task task = TaskCreator.createTaskToBeSaved();

        Task taskSaved = this.taskRepository.save(task);

        List<Task> tasks = this.taskRepository.findByTitle(taskSaved.getTitle());

        Assertions.assertThat(tasks)
                .isNotEmpty()
                .contains(taskSaved);
    }

    @Test
    @DisplayName("Should not be able to find a nonexistent task")
    void findByTitleReturnsEmptyList() {
        List<Task> tasks = this.taskRepository.findByTitle("Null task");

        Assertions.assertThat(tasks).isEmpty();
    }

    @Test
    @DisplayName("Should not be able to set an empty task title")
    void createThrowConstraintViolationExceptionTest() {
        Task task = new Task();

        try(ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            Validator validator = factory.getValidator();
            Set<ConstraintViolation<Task>> violations = validator.validateProperty(task, "title");
            Assertions.assertThat(violations)
                    .as("The title cannot be empty")
                    .isNotEmpty();
        }
    }
}