package com.spring.crujar.controller.request;

import lombok.Data;

@Data
public class TaskPostRequest {
    private String title;
    private String description;
}
