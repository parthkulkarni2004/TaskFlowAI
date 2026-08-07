package com.taskflow.backend.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class CalendarEventRequest {
    @NotBlank
    private String label;
    @NotNull
    private LocalDate date;
    private String color;
    private Long projectId;
}
