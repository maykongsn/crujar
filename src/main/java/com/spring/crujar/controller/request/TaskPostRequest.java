package com.spring.crujar.controller.request;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class TaskPostRequest {
    @NotEmpty(message = "The title cannot be empty")
    private String title;
    private String description;
}
