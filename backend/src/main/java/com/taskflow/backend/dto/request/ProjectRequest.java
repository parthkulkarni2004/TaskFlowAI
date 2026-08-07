package com.taskflow.backend.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class ProjectRequest {
    @NotBlank
    private String name;
    private String desc;
    private LocalDate due;
    private String status; // Active | On Hold | Completed
    private Integer progress;
    private List<Long> teamUserIds;
}
