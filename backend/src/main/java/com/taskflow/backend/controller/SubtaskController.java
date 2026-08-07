package com.taskflow.backend.controller;

import com.taskflow.backend.dto.response.SubtaskResponse;
import com.taskflow.backend.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/subtasks")
@RequiredArgsConstructor
public class SubtaskController {

    private final TaskService taskService;

    @PatchMapping("/{id}/toggle")
    public ResponseEntity<SubtaskResponse> toggle(@PathVariable Long id) {
        return ResponseEntity.ok(taskService.toggleSubtask(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        taskService.deleteSubtask(id);
        return ResponseEntity.noContent().build();
    }
}
