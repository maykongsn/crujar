package com.spring.crujar.util;

import com.spring.crujar.controller.request.TaskPutRequest;
import com.spring.crujar.domain.Task;

public class TaskPutRequestCreator {
    public static TaskPutRequest createTaskPutRequest() {
        Task task = TaskCreator.createValidUpdatedTask();

        return TaskPutRequest.builder()
                .id(task.getId())
                .title(task.getTitle())
                .description(task.getDescription())
                .build();
    }
}
