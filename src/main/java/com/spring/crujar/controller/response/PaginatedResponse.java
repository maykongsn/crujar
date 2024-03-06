package com.spring.crujar.controller.response;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

public class PaginatedResponse<T> extends PageImpl<T> {
    private boolean first;
    private boolean last;
    private int totalPages;
    private int numberOfElements;

    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public PaginatedResponse(@JsonProperty("content") List<T> content,
                             @JsonProperty("number") int number,
                             @JsonProperty("size") int size,
                             @JsonProperty("totalElements") int totalElements,
                             @JsonProperty("last") boolean last,
                             @JsonProperty("first") boolean first,
                             @JsonProperty("pageable") JsonNode pageable,
                             @JsonProperty("sort") JsonNode sort,
                             @JsonProperty("numberOfElements") int numberOfElements,
                             @JsonProperty("totalPages") int totalPages) {
        super(content, PageRequest.of(number, size), totalPages);

        this.last = last;
        this.first = first;
        this.totalPages = totalElements;
        this.numberOfElements = numberOfElements;
    }

    public PaginatedResponse(List<T> content, Pageable pageable, long total) {
        super(content, pageable, total);
    }
}
