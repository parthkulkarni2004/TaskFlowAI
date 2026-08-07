package com.taskflow.backend.dto.response;

import com.taskflow.backend.entity.Notification;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationResponse {
    private Long id;
    private String icon;
    private String text;
    private boolean unread;
    private LocalDateTime createdAt;

    public static NotificationResponse from(Notification n) {
        return NotificationResponse.builder()
                .id(n.getId())
                .icon(n.getIcon())
                .text(n.getText())
                .unread(n.isUnread())
                .createdAt(n.getCreatedAt())
                .build();
    }
}
