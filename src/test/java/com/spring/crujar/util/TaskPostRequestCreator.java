package com.spring.crujar.util;

import com.spring.crujar.controller.request.TaskPostRequest;
import com.spring.crujar.domain.Task;

public class TaskPostRequestCreator {
    public static TaskPostRequest createTaskPostRequest() {
        Task task = TaskCreator.createTaskToBeSaved();

        return TaskPostRequest.builder()
                .title(task.getTitle())
                .description(task.getDescription())
                .build();
    }
}
