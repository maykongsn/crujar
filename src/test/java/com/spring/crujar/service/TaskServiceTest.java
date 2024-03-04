package com.spring.crujar.service;

import com.spring.crujar.controller.TaskController;
import com.spring.crujar.controller.request.TaskPostRequest;
import com.spring.crujar.controller.request.TaskPutRequest;
import com.spring.crujar.domain.Task;
import com.spring.crujar.exception.BadRequestException;
import com.spring.crujar.repository.TaskRepository;
import com.spring.crujar.util.TaskCreator;
import com.spring.crujar.util.TaskPostRequestCreator;
import com.spring.crujar.util.TaskPutRequestCreator;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@ExtendWith(SpringExtension.class)
public class TaskServiceTest {
    @InjectMocks
    private TaskService taskService;

    @Mock
    private TaskRepository taskRepositoryMock;

    @BeforeEach
    void setUp() {
        PageImpl<Task> taskPage = new PageImpl<>(List.of(TaskCreator.createValidTask()));

        BDDMockito.when(taskRepositoryMock.findAll(ArgumentMatchers.any(PageRequest.class)))
                .thenReturn(taskPage);

        BDDMockito.when(taskRepositoryMock.findByTitle(ArgumentMatchers.anyString()))
                .thenReturn(List.of(TaskCreator.createValidTask()));

        BDDMockito.doNothing().when(taskRepositoryMock).delete(ArgumentMatchers.any(Task.class));
    }

    @Test
    @DisplayName("Should be able to list all tasks")
    void listAllPageableTasksTest() {
        String expectedTitle = TaskCreator.createValidTask().getTitle();

        Page<Task> taskPage = taskService.listAll(PageRequest.of(1, 1));

        Assertions.assertThat(taskPage).isNotNull();
        Assertions.assertThat(taskPage.toList())
                .isNotEmpty()
                .hasSize(1);
        Assertions.assertThat(taskPage.toList()
                .get(0)
                .getTitle()).isEqualTo(expectedTitle);
    }

    @Test
    @DisplayName("Should be able to find a task by id")
    void findTaskByIdTest() {
        Task task = TaskCreator.createValidTask();
        UUID expectedUUID = task.getId();

        BDDMockito.when(taskRepositoryMock.findById(ArgumentMatchers.any(UUID.class)))
                .thenReturn(Optional.of(task));

        Task taskSaved = taskService.findById(task.getId());

        Assertions.assertThat(taskSaved).isNotNull();
        Assertions.assertThat(taskSaved.getId()).isNotNull().isEqualTo(expectedUUID);
    }

    @Test
    @DisplayName("Should not to be able to find a task when id is not found")
    void findTaskByIdNotFoundTest() {
        BDDMockito.when(taskRepositoryMock.findById(ArgumentMatchers.any(UUID.class)))
                .thenReturn(Optional.empty());

        Assertions.assertThatExceptionOfType(BadRequestException.class)
                .isThrownBy(() -> this.taskService.findById(UUID.randomUUID()));
    }

    @Test
    @DisplayName("Should be able to list tasks by name")
    void findTaskByNameTest() {
        String expectedTitle = TaskCreator.createValidTask().getTitle();

        List<Task> tasks = taskService.findByTitle("task");

        Assertions.assertThat(tasks).isNotNull().isNotEmpty().hasSize(1);
        Assertions.assertThat(tasks.get(0).getTitle()).isEqualTo(expectedTitle);
    }

    @Test
    @DisplayName("Should not be able to list when title is not found")
    void findTaskByTitleNotFound() {
        BDDMockito.when(taskRepositoryMock.findByTitle(ArgumentMatchers.anyString()))
                .thenReturn(Collections.emptyList());

        List<Task> tasks = taskService.findByTitle("task");

        Assertions.assertThat(tasks).isNotNull().isEmpty();
    }

    @Test
    @DisplayName("Should be able to save a task")
    void saveTaskTest() {
        Task task = TaskCreator.createValidTask();

        BDDMockito.when(taskRepositoryMock.save(ArgumentMatchers.any(Task.class)))
                .thenReturn(task);

        Task taskSaved = taskService.save(TaskPostRequestCreator.createTaskPostRequest());

        Assertions.assertThat(task).isNotNull().isEqualTo(taskSaved);
    }

    @Test
    @DisplayName("Should be able to update a task")
    void updateTaskTest() {
        BDDMockito.when(taskRepositoryMock.findById(ArgumentMatchers.any(UUID.class)))
                .thenReturn(Optional.of(TaskCreator.createValidTask()));

        Assertions.assertThatCode(() -> taskService.replace(TaskPutRequestCreator.createTaskPutRequest()))
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("Should be able to delete a task")
    void deleteTaskTest() {
        BDDMockito.when(taskRepositoryMock.findById(ArgumentMatchers.any(UUID.class)))
                .thenReturn(Optional.of(TaskCreator.createValidTask()));

        Assertions.assertThatCode(() -> taskService.delete(UUID.randomUUID()))
                .doesNotThrowAnyException();
    }
}
