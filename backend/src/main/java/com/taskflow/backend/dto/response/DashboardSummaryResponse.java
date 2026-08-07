package com.taskflow.backend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DashboardSummaryResponse {
    private long totalProjects;
    private long totalTasks;
    private long completedTasks;
    private long overdueTasks;
    private List<ActivityResponse> activity;
    private List<DeadlineResponse> deadlines;
    private List<ProjectResponse> topProjects;
    private Map<String, Integer> weeklyTaskCompletion; // day -> percent/count
}
