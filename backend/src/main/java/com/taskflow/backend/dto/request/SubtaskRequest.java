package com.taskflow.backend.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class SubtaskRequest {
    @NotBlank
    private String title;
}
