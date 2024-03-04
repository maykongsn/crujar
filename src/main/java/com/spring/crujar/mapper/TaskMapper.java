package com.spring.crujar.mapper;

import com.spring.crujar.controller.request.TaskPostRequest;
import com.spring.crujar.controller.request.TaskPutRequest;
import com.spring.crujar.domain.Task;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface TaskMapper {
    TaskMapper INSTANCE = Mappers.getMapper(TaskMapper.class);
    Task toTask(TaskPostRequest taskPostRequest);
    Task toTask(TaskPutRequest taskPutRequest);
}
