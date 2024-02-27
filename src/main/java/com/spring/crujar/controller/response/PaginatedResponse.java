package com.spring.crujar.controller.response;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

public class PaginatedResponse<T> extends PageImpl<T> {
    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public PaginatedResponse(@JsonProperty("content") List<T> content,
                             @JsonProperty("number") int number,
                             @JsonProperty("size") int size,
                             @JsonProperty("pageable") JsonNode pageable,
                             @JsonProperty("totalPages") int totalPages) {
        super(content, PageRequest.of(number, size), totalPages);
    }

    public PaginatedResponse(List<T> content, Pageable pageable, long total) {
        super(content, pageable, total);
    }
}
