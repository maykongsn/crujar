package com.spring.crujar.controller.request;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class TaskPutRequest {
    private UUID id;
    private String title;
    private String description;
}
