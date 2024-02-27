package com.spring.crujar.client;

import com.spring.crujar.controller.response.PaginatedResponse;
import com.spring.crujar.domain.Task;
import lombok.extern.log4j.Log4j2;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Log4j2
public class SpringClient {
    public static void main(String[] args) {
        ResponseEntity<Task> entity = new RestTemplate().getForEntity("http://localhost:8080/tasks/{id}", Task.class, "625d2645-0a6b-4e67-8bb9-09361b62da09");
        log.info(entity);

        Task obj = new RestTemplate().getForObject("http://localhost:8080/tasks/{id}", Task.class, "625d2645-0a6b-4e67-8bb9-09361b62da09");
        log.info(obj);

        ParameterizedTypeReference<PaginatedResponse<Task>> responseType = new
                ParameterizedTypeReference<>() {};

        ResponseEntity<PaginatedResponse<Task>> result = new RestTemplate()
                .exchange("http://localhost:8080/tasks",
                        HttpMethod.GET,
                        null,
                        responseType);

        List<Task> tasks = result.getBody().getContent();
        log.info(tasks);
    }
}
