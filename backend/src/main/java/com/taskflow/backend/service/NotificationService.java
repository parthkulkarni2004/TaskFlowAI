package com.taskflow.backend.service;

import com.taskflow.backend.entity.Notification;
import com.taskflow.backend.entity.User;
import com.taskflow.backend.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;

    public void notify(User user, String icon, String text) {
        if (user == null) return;
        Notification n = Notification.builder()
                .user(user)
                .icon(icon)
                .text(text)
                .build();
        notificationRepository.save(n);
    }
}
