package com.taskflow.backend.service;

import com.taskflow.backend.dto.response.ReportResponse;
import com.taskflow.backend.dto.response.TaskResponse;
import com.taskflow.backend.entity.Task;
import com.taskflow.backend.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ReportService {

    private final TaskRepository taskRepository;

    @Transactional
    public ReportResponse generate() {
        List<Task> all = taskRepository.findAll();
        long total = all.size();
        long completed = all.stream().filter(t -> t.getStatus() == Task.TaskStatus.COMPLETED).count();
        long inProgress = all.stream().filter(t -> t.getStatus() == Task.TaskStatus.IN_PROGRESS).count();
        LocalDate today = LocalDate.now();
        long overdue = all.stream().filter(t -> t.getDueDate() != null && t.getDueDate().isBefore(today)
                && t.getStatus() != Task.TaskStatus.COMPLETED).count();

        Map<String, Long> byStatus = new LinkedHashMap<>();
        for (Task.TaskStatus s : Task.TaskStatus.values()) {
            byStatus.put(TaskResponse.humanStatus(s.name()),
                    all.stream().filter(t -> t.getStatus() == s).count());
        }

        Map<String, Long> byPriority = new LinkedHashMap<>();
        for (Task.TaskPriority p : Task.TaskPriority.values()) {
            byPriority.put(TaskResponse.humanPriority(p.name()),
                    all.stream().filter(t -> t.getPriority() == p).count());
        }

        Map<String, Integer> productivity = new LinkedHashMap<>();
        LocalDate monday = today.with(DayOfWeek.MONDAY);
        for (int i = 0; i < 7; i++) {
            LocalDate day = monday.plusDays(i);
            long dayTotal = all.stream().filter(t -> t.getCreatedAt() != null && t.getCreatedAt().toLocalDate().equals(day)).count();
            long dayDone = all.stream().filter(t -> t.getCreatedAt() != null && t.getCreatedAt().toLocalDate().equals(day)
                    && t.getStatus() == Task.TaskStatus.COMPLETED).count();
            int pct = dayTotal == 0 ? 0 : (int) Math.round((dayDone * 100.0) / dayTotal);
            productivity.put(day.getDayOfWeek().getDisplayName(TextStyle.SHORT, Locale.ENGLISH), pct);
        }

        return ReportResponse.builder()
                .totalTasks(total)
                .completedTasks(completed)
                .inProgressTasks(inProgress)
                .overdueTasks(overdue)
                .teamProductivity(productivity)
                .tasksByStatus(byStatus)
                .tasksByPriority(byPriority)
                .build();
    }
}
