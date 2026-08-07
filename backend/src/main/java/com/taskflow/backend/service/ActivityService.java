package com.taskflow.backend.service;

import com.taskflow.backend.entity.ActivityLog;
import com.taskflow.backend.entity.User;
import com.taskflow.backend.repository.ActivityLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ActivityService {

    private final ActivityLogRepository activityLogRepository;

    public void log(String icon, String title, User user) {
        ActivityLog log = ActivityLog.builder()
                .icon(icon)
                .title(title)
                .user(user)
                .build();
        activityLogRepository.save(log);
    }
}
