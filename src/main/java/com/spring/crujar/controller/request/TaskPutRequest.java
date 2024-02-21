package com.spring.crujar.controller.request;

import lombok.Data;

import java.util.UUID;

@Data
public class TaskPutRequest {
    private UUID id;
    private String title;
    private String description;
}
