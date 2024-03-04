package com.spring.crujar.domain;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "tasks")
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(length = 16)
    private UUID id;

    @NotEmpty(message = "The title cannot be empty")
    private String title;

    private String description;
}
