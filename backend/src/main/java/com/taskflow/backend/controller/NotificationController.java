package com.taskflow.backend.controller;

import com.taskflow.backend.dto.response.NotificationResponse;
import com.taskflow.backend.entity.Notification;
import com.taskflow.backend.repository.NotificationRepository;
import com.taskflow.backend.exception.ResourceNotFoundException;
import com.taskflow.backend.security.CurrentUser;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationRepository notificationRepository;
    private final CurrentUser currentUser;

    @GetMapping
    public ResponseEntity<List<NotificationResponse>> listAll() {
        return ResponseEntity.ok(
                notificationRepository.findByUser_IdOrderByCreatedAtDesc(currentUser.id())
                        .stream().map(NotificationResponse::from).collect(Collectors.toList())
        );
    }

    @GetMapping("/unread-count")
    public ResponseEntity<Map<String, Long>> unreadCount() {
        return ResponseEntity.ok(Map.of("count", notificationRepository.countByUser_IdAndUnreadTrue(currentUser.id())));
    }

    @PutMapping("/{id}/read")
    public ResponseEntity<NotificationResponse> markRead(@PathVariable Long id) {
        Notification n = notificationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Notification not found: " + id));
        n.setUnread(false);
        return ResponseEntity.ok(NotificationResponse.from(notificationRepository.save(n)));
    }

    @PutMapping("/read-all")
    public ResponseEntity<Void> markAllRead() {
        List<Notification> all = notificationRepository.findByUser_IdOrderByCreatedAtDesc(currentUser.id());
        all.forEach(n -> n.setUnread(false));
        notificationRepository.saveAll(all);
        return ResponseEntity.noContent().build();
    }
}
