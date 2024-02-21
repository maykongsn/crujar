package com.spring.crujar.mapper;

import com.spring.crujar.controller.request.TaskPostRequest;
import com.spring.crujar.controller.request.TaskPutRequest;
import com.spring.crujar.domain.Task;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TaskMapper {
    Task toTask(TaskPostRequest taskPostRequest);
    Task toTask(TaskPutRequest taskPutRequest);
}
