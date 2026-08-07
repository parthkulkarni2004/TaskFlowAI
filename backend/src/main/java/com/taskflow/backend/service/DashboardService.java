package com.taskflow.backend.service;

import com.taskflow.backend.dto.response.*;
import com.taskflow.backend.entity.ActivityLog;
import com.taskflow.backend.entity.Project;
import com.taskflow.backend.entity.Task;
import com.taskflow.backend.repository.ActivityLogRepository;
import com.taskflow.backend.repository.ProjectRepository;
import com.taskflow.backend.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final ProjectRepository projectRepository;
    private final TaskRepository taskRepository;
    private final ActivityLogRepository activityLogRepository;

    @Transactional
    public DashboardSummaryResponse summary() {
        long totalProjects = projectRepository.count();
        long totalTasks = taskRepository.count();
        long completedTasks = taskRepository.countByStatus(Task.TaskStatus.COMPLETED);

        LocalDate today = LocalDate.now();
        long overdueTasks = taskRepository.findAll().stream()
                .filter(t -> t.getDueDate() != null && t.getDueDate().isBefore(today) && t.getStatus() != Task.TaskStatus.COMPLETED)
                .count();

        List<ActivityResponse> activity = activityLogRepository.findAllByOrderByCreatedAtDesc(PageRequest.of(0, 6))
                .stream().map(ActivityResponse::from).collect(Collectors.toList());

        List<DeadlineResponse> deadlines = taskRepository.findAll().stream()
                .filter(t -> t.getDueDate() != null && t.getStatus() != Task.TaskStatus.COMPLETED)
                .sorted((a, b) -> a.getDueDate().compareTo(b.getDueDate()))
                .limit(5)
                .map(t -> DeadlineResponse.builder()
                        .title(t.getName())
                        .due(dueLabel(t.getDueDate()))
                        .priority(TaskResponse.humanPriority(t.getPriority().name()))
                        .build())
                .collect(Collectors.toList());

        List<ProjectResponse> topProjects = projectRepository.findAllByOrderByCreatedAtDesc().stream()
                .sorted((a, b) -> Integer.compare(b.getProgress(), a.getProgress()))
                .limit(3)
                .map(ProjectResponse::from)
                .collect(Collectors.toList());

        Map<String, Integer> weekly = weeklyCompletion();

        return DashboardSummaryResponse.builder()
                .totalProjects(totalProjects)
                .totalTasks(totalTasks)
                .completedTasks(completedTasks)
                .overdueTasks(overdueTasks)
                .activity(activity)
                .deadlines(deadlines)
                .topProjects(topProjects)
                .weeklyTaskCompletion(weekly)
                .build();
    }

    private Map<String, Integer> weeklyCompletion() {
        LocalDate today = LocalDate.now();
        LocalDate monday = today.with(DayOfWeek.MONDAY);
        java.util.LinkedHashMap<String, Integer> map = new java.util.LinkedHashMap<>();
        List<Task> all = taskRepository.findAll();
        for (int i = 0; i < 7; i++) {
            LocalDate day = monday.plusDays(i);
            long total = all.stream().filter(t -> t.getCreatedAt() != null && t.getCreatedAt().toLocalDate().equals(day)).count();
            long done = all.stream().filter(t -> t.getCreatedAt() != null && t.getCreatedAt().toLocalDate().equals(day)
                    && t.getStatus() == Task.TaskStatus.COMPLETED).count();
            int percent = total == 0 ? 0 : (int) Math.round((done * 100.0) / total);
            map.put(day.getDayOfWeek().getDisplayName(TextStyle.SHORT, Locale.ENGLISH), percent);
        }
        return map;
    }

    private String dueLabel(LocalDate due) {
        long totalDays = java.time.temporal.ChronoUnit.DAYS.between(LocalDate.now(), due);
        if (totalDays < 0) return "Overdue";
        if (totalDays == 0) return "Due today";
        if (totalDays == 1) return "Due in 1 day";
        return "Due in " + totalDays + " days";
    }
}
