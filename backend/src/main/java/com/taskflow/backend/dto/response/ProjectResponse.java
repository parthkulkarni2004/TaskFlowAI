package com.taskflow.backend.dto.response;

import com.taskflow.backend.entity.Project;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProjectResponse {
    private Long id;
    private String name;
    private String desc;
    private LocalDate due;
    private String status;
    private int progress;
    private List<UserResponse> team;
    private int taskCount;

    public static ProjectResponse from(Project p) {
        return ProjectResponse.builder()
                .id(p.getId())
                .name(p.getName())
                .desc(p.getDescription())
                .due(p.getDueDate())
                .status(humanStatus(p.getStatus().name()))
                .progress(p.getProgress())
                .team(p.getTeam().stream().map(UserResponse::from).collect(Collectors.toList()))
                .build();
    }

    private static String humanStatus(String enumName) {
        return switch (enumName) {
            case "ON_HOLD" -> "On Hold";
            case "COMPLETED" -> "Completed";
            default -> "Active";
        };
    }
}
