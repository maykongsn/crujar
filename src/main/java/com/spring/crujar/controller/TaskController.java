package com.spring.crujar.controller;

import com.spring.crujar.controller.request.TaskPostRequest;
import com.spring.crujar.controller.request.TaskPutRequest;
import com.spring.crujar.domain.Task;
import com.spring.crujar.service.TaskService;
import com.spring.crujar.util.DateUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("tasks")
@Log4j2
@RequiredArgsConstructor
public class TaskController {
    private final DateUtil dateUtil;
    private final TaskService taskService;

    @GetMapping
    public ResponseEntity<List<Task>> list() {
        log.info(dateUtil.formatLocalDateTimeToDatabaseStyle(LocalDateTime.now()));
        return new ResponseEntity<>(taskService.listAll(), HttpStatus.OK);
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<Task> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(taskService.findById(id));
    }

    @PostMapping
    public ResponseEntity<Task> save(@RequestBody TaskPostRequest taskPostRequest) {
        return new ResponseEntity<>(taskService.save(taskPostRequest), HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<Void> replace(@RequestBody TaskPutRequest taskPutRequest) {
        taskService.replace(taskPutRequest);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        taskService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
