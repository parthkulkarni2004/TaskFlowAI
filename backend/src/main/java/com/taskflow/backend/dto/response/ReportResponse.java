package com.taskflow.backend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReportResponse {
    private long totalTasks;
    private long completedTasks;
    private long inProgressTasks;
    private long overdueTasks;
    private Map<String, Integer> teamProductivity; // day -> percent
    private Map<String, Long> tasksByStatus;
    private Map<String, Long> tasksByPriority;
}
