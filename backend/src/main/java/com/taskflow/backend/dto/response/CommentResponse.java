package com.taskflow.backend.dto.response;

import com.taskflow.backend.entity.Comment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentResponse {
    private Long id;
    private String text;
    private String userName;
    private String userAvatar;
    private LocalDateTime createdAt;

    public static CommentResponse from(Comment c) {
        return CommentResponse.builder()
                .id(c.getId())
                .text(c.getText())
                .userName(c.getUser().getFullName())
                .userAvatar(c.getUser().getAvatarUrl())
                .createdAt(c.getCreatedAt())
                .build();
    }
}
