package com.taskflow.backend.dto.response;

import com.taskflow.backend.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {
    private Long id;
    private String fullName;
    private String email;
    private String role;
    private String phone;
    private String location;
    private String bio;
    private String avatarUrl;

    public static UserResponse from(User u) {
        if (u == null) return null;
        return UserResponse.builder()
                .id(u.getId())
                .fullName(u.getFullName())
                .email(u.getEmail())
                .role(u.getRole())
                .phone(u.getPhone())
                .location(u.getLocation())
                .bio(u.getBio())
                .avatarUrl(u.getAvatarUrl())
                .build();
    }
}
