package com.spring.crujar.integration;

import com.spring.crujar.controller.request.TaskPostRequest;
import com.spring.crujar.controller.response.PaginatedResponse;
import com.spring.crujar.domain.Task;
import com.spring.crujar.repository.TaskRepository;
import com.spring.crujar.util.TaskCreator;
import com.spring.crujar.util.TaskPostRequestCreator;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;

import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class TaskControllerIntegrationTest {
    @Autowired
    private TestRestTemplate testRestTemplate;

    @LocalServerPort
    private int port;

    @Autowired
    private TaskRepository taskRepository;

    @Test
    @DisplayName("Should be able to list all tasks")
    void listAllTasksTest() {
        Task taskSaved = taskRepository.save(TaskCreator.createTaskToBeSaved());

        ParameterizedTypeReference<PaginatedResponse<Task>> responseType = new
                ParameterizedTypeReference<>() {};

        PaginatedResponse<Task> taskPage = testRestTemplate
                .exchange("/tasks",
                        HttpMethod.GET,
                        null,
                        responseType).getBody();

        Assertions.assertThat(taskPage).isNotNull();
        Assertions.assertThat(taskPage.toList()).isNotEmpty().hasSize(1);
        Assertions.assertThat(taskPage.toList().get(0).getTitle()).isEqualTo(taskSaved.getTitle());
    }

    @Test
    @DisplayName("Should be able to find a task by id")
    void findTaskById() {
        Task taskSaved = taskRepository.save(TaskCreator.createValidTask());

        Task task = testRestTemplate.getForObject("/tasks/{id}", Task.class, taskSaved.getId());

        Assertions.assertThat(task).isNotNull();

        Assertions.assertThat(task.getId()).isNotNull().isEqualTo(taskSaved.getId());
    }

    @Test
    @DisplayName("Should be able to find a task by title")
    void findTaskByTitle() {
        Task taskSaved = taskRepository.save(TaskCreator.createTaskToBeSaved());

        String url = String.format("/tasks/find?title=%s", taskSaved.getTitle());
        List<Task> tasks = testRestTemplate.exchange(url, HttpMethod.GET, null,
                new ParameterizedTypeReference<List<Task>>() {
        }).getBody();

        Assertions.assertThat(tasks)
                .isNotNull()
                .isNotEmpty()
                .hasSize(1);

        Assertions.assertThat(tasks.get(0).getTitle()).isEqualTo(taskSaved.getTitle());
    }

    @Test
    @DisplayName("Should not be able to list when title is not found")
    void findTaskByTitleNotFoundTest() {
        List<Task> tasks = testRestTemplate.exchange("/tasks/find?title=task", HttpMethod.GET, null,
                new ParameterizedTypeReference<List<Task>>() {
        }).getBody();

        Assertions.assertThat(tasks).isNotNull().isEmpty();
    }

    @Test
    @DisplayName("Should be able to save a task")
    void saveTaskTest() {
        TaskPostRequest taskPostRequest = TaskPostRequestCreator.createTaskPostRequest();

        ResponseEntity<Task> taskResponseEntity = testRestTemplate.postForEntity("/tasks", taskPostRequest, Task.class);

        Assertions.assertThat(taskResponseEntity).isNotNull();
        Assertions.assertThat(taskResponseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        Assertions.assertThat(taskResponseEntity.getBody()).isNotNull();
        Assertions.assertThat(taskResponseEntity.getBody().getId()).isNotNull();
    }

    @Test
    @DisplayName("Should be able to update a task")
    void updateTaskTest() {
        Task taskSaved = taskRepository.save(TaskCreator.createTaskToBeSaved());
        taskSaved.setTitle("New title");

        ResponseEntity<Void> taskResponseEntity = testRestTemplate.exchange("/tasks",
                HttpMethod.PUT, new HttpEntity<>(taskSaved), Void.class);

        Assertions.assertThat(taskResponseEntity).isNotNull();
        Assertions.assertThat(taskResponseEntity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

    @Test
    @DisplayName("Should be able to delete a task")
    void deleteTaskTest() {
        Task taskSaved = taskRepository.save(TaskCreator.createTaskToBeSaved());

        ResponseEntity<Void> taskResponseEntity = testRestTemplate.exchange("/tasks/{id}",
                HttpMethod.DELETE, new HttpEntity<>(taskSaved), Void.class, taskSaved.getId());

        Assertions.assertThat(taskResponseEntity).isNotNull();
        Assertions.assertThat(taskResponseEntity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }
}
