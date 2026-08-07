package com.taskflow.backend.dto.response;

import com.taskflow.backend.entity.Task;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaskResponse {
    private Long id;
    private String name;
    private String desc;
    private Long projectId;
    private String project;
    private Long assigneeId;
    private String assignee;
    private String assigneeAvatar;
    private LocalDate due;
    private String status;
    private String priority;
    private int subtaskTotal;
    private int subtaskDone;
    private int commentCount;

    public static TaskResponse from(Task t) {
        return TaskResponse.builder()
                .id(t.getId())
                .name(t.getName())
                .desc(t.getDescription())
                .projectId(t.getProject() != null ? t.getProject().getId() : null)
                .project(t.getProject() != null ? t.getProject().getName() : null)
                .assigneeId(t.getAssignee() != null ? t.getAssignee().getId() : null)
                .assignee(t.getAssignee() != null ? t.getAssignee().getFullName() : null)
                .assigneeAvatar(t.getAssignee() != null ? t.getAssignee().getAvatarUrl() : null)
                .due(t.getDueDate())
                .status(humanStatus(t.getStatus().name()))
                .priority(humanPriority(t.getPriority().name()))
                .subtaskTotal(t.getSubtasks() == null ? 0 : t.getSubtasks().size())
                .subtaskDone(t.getSubtasks() == null ? 0 : (int) t.getSubtasks().stream().filter(s -> s.isCompleted()).count())
                .commentCount(t.getComments() == null ? 0 : t.getComments().size())
                .build();
    }

    public static String humanStatus(String enumName) {
        return switch (enumName) {
            case "IN_PROGRESS" -> "In Progress";
            case "REVIEW" -> "Review";
            case "COMPLETED" -> "Completed";
            default -> "To Do";
        };
    }

    public static String humanPriority(String enumName) {
        return switch (enumName) {
            case "HIGH" -> "High";
            case "LOW" -> "Low";
            default -> "Medium";
        };
    }
}
