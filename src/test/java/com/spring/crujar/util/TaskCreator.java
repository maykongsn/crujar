package com.spring.crujar.util;

import com.spring.crujar.domain.Task;

import java.util.UUID;

public class TaskCreator {
    public static Task createTaskToBeSaved() {
        return Task.builder()
                .title("My Task")
                .description("New Task")
                .build();
    }

    public static Task createValidTask() {
        return Task.builder()
                .id(UUID.randomUUID())
                .title("My Task")
                .description("New Task")
                .build();
    }

    public static Task createValidUpdatedTask() {
        return Task.builder()
                .title("My Task 2")
                .description("My second task")
                .id(UUID.randomUUID())
                .build();
    }
}
