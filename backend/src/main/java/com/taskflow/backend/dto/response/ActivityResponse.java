package com.taskflow.backend.dto.response;

import com.taskflow.backend.entity.ActivityLog;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ActivityResponse {
    private String icon;
    private String title;
    private String time;

    public static ActivityResponse from(ActivityLog a) {
        return ActivityResponse.builder()
                .icon(a.getIcon())
                .title(a.getTitle())
                .time(com.taskflow.backend.util.TimeAgo.format(a.getCreatedAt()))
                .build();
    }
}
