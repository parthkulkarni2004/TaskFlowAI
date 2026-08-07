package com.taskflow.backend.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class TaskRequest {
    @NotBlank
    private String name;
    private String desc;
    @NotNull
    private Long projectId;
    private Long assigneeId;
    private LocalDate due;
    private String status;   // To Do | In Progress | Review | Completed
    private String priority; // Low | Medium | High
}
