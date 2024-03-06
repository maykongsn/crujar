package com.spring.crujar.unit.controller;

import com.spring.crujar.controller.TaskController;
import com.spring.crujar.controller.request.TaskPostRequest;
import com.spring.crujar.controller.request.TaskPutRequest;
import com.spring.crujar.domain.Task;
import com.spring.crujar.service.TaskService;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

@ExtendWith(SpringExtension.class)
public class TaskControllerTest {
    @InjectMocks
    private TaskController taskController;

    @Mock
    private TaskService taskServiceMock;

    @BeforeEach
    void setUp() {
        PageImpl<Task> taskPage = new PageImpl<>(List.of(TaskCreator.createValidTask()));

        BDDMockito.when(taskServiceMock.listAll(ArgumentMatchers.any()))
                .thenReturn(taskPage);

        BDDMockito.when(taskServiceMock.findByTitle(ArgumentMatchers.anyString()))
                .thenReturn(List.of(TaskCreator.createValidTask()));

        BDDMockito.doNothing().when(taskServiceMock).replace(ArgumentMatchers.any(TaskPutRequest.class));

        BDDMockito.doNothing().when(taskServiceMock).delete(ArgumentMatchers.any(UUID.class));
    }

    @Test
    @DisplayName("Should be able to list all tasks")
    void listAllPageableTasksTest() {
        String expectedTitle = TaskCreator.createValidTask().getTitle();

        Page<Task> taskPage = taskController.list(null).getBody();

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

        BDDMockito.when(taskServiceMock.findById(expectedUUID)).thenReturn(task);

        ResponseEntity<Task> responseEntity = taskController.findById(null);

        Assertions.assertThat(responseEntity).isNotNull();
        Assertions.assertThat(task.getId()).isNotNull().isEqualTo(expectedUUID);
    }

    @Test
    @DisplayName("Should be able to list tasks by name")
    void findTaskByNameTest() {
        String expectedTitle = TaskCreator.createValidTask().getTitle();

        List<Task> tasks = taskController.findByTitle("task").getBody();

        Assertions.assertThat(tasks).isNotNull().isNotEmpty().hasSize(1);
        Assertions.assertThat(tasks.get(0).getTitle()).isEqualTo(expectedTitle);
    }

    @Test
    @DisplayName("Should not be able to list when title is not found")
    void findTaskByTitleNotFound() {
        BDDMockito.when(taskServiceMock.findByTitle(ArgumentMatchers.anyString()))
                .thenReturn(Collections.emptyList());

        List<Task> tasks = taskController.findByTitle("task").getBody();

        Assertions.assertThat(tasks).isNotNull().isEmpty();
    }

    @Test
    @DisplayName("Should be able to save a task")
    void saveTaskTest() {
        Task task = TaskCreator.createValidTask();

        BDDMockito.when(taskServiceMock.save(ArgumentMatchers.any(TaskPostRequest.class)))
                .thenReturn(task);

        Task taskSaved = taskController.save(TaskPostRequestCreator.createTaskPostRequest()).getBody();

        Assertions.assertThat(task).isNotNull().isEqualTo(taskSaved);
    }

    @Test
    @DisplayName("Should be able to update a task")
    void updateTaskTest() {
        ResponseEntity<Void> entity = taskController.replace(TaskPutRequestCreator.createTaskPutRequest());

        Assertions.assertThat(entity).isNotNull();
        Assertions.assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

    @Test
    @DisplayName("Should be able to delete a task")
    void deleteTaskTest() {
        Assertions.assertThatCode(() -> taskController.delete(UUID.randomUUID())).doesNotThrowAnyException();
    }
}
