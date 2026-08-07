package com.taskflow.backend.dto.response;

import com.taskflow.backend.entity.Subtask;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SubtaskResponse {
    private Long id;
    private String title;
    private boolean completed;

    public static SubtaskResponse from(Subtask s) {
        return SubtaskResponse.builder()
                .id(s.getId())
                .title(s.getTitle())
                .completed(s.isCompleted())
                .build();
    }
}
