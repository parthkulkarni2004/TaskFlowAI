package com.taskflow.backend.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class TaskStatusUpdateRequest {
    @NotBlank
    private String status; // To Do | In Progress | Review | Completed
}
