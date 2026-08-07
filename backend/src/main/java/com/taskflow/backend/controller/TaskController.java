package com.taskflow.backend.controller;

import com.taskflow.backend.dto.request.*;
import com.taskflow.backend.dto.response.CommentResponse;
import com.taskflow.backend.dto.response.SubtaskResponse;
import com.taskflow.backend.dto.response.TaskResponse;
import com.taskflow.backend.security.CurrentUser;
import com.taskflow.backend.service.TaskService;
import com.taskflow.backend.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;
    private final UserService userService;
    private final CurrentUser currentUser;

    @GetMapping
    public ResponseEntity<List<TaskResponse>> listAll(
            @RequestParam(required = false) Long projectId,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String priority,
            @RequestParam(required = false) Long assigneeId,
            @RequestParam(required = false) String search) {
        return ResponseEntity.ok(taskService.listAll(projectId, status, priority, assigneeId, search));
    }

    @GetMapping("/kanban")
    public ResponseEntity<Map<String, List<TaskResponse>>> kanban() {
        return ResponseEntity.ok(taskService.kanbanBoard());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskResponse> getOne(@PathVariable Long id) {
        return ResponseEntity.ok(taskService.getOne(id));
    }

    @PostMapping
    public ResponseEntity<TaskResponse> create(@Valid @RequestBody TaskRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(taskService.create(request, userService.getById(currentUser.id())));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TaskResponse> update(@PathVariable Long id, @Valid @RequestBody TaskRequest request) {
        return ResponseEntity.ok(taskService.update(id, request, userService.getById(currentUser.id())));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<TaskResponse> updateStatus(@PathVariable Long id, @Valid @RequestBody TaskStatusUpdateRequest request) {
        return ResponseEntity.ok(taskService.updateStatus(id, request.getStatus(), userService.getById(currentUser.id())));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        taskService.delete(id);
        return ResponseEntity.noContent().build();
    }

    // ---------- Subtasks ----------
    @GetMapping("/{id}/subtasks")
    public ResponseEntity<List<SubtaskResponse>> listSubtasks(@PathVariable Long id) {
        return ResponseEntity.ok(taskService.listSubtasks(id));
    }

    @PostMapping("/{id}/subtasks")
    public ResponseEntity<SubtaskResponse> addSubtask(@PathVariable Long id, @Valid @RequestBody SubtaskRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(taskService.addSubtask(id, request));
    }

    // ---------- Comments ----------
    @GetMapping("/{id}/comments")
    public ResponseEntity<List<CommentResponse>> listComments(@PathVariable Long id) {
        return ResponseEntity.ok(taskService.listComments(id));
    }

    @PostMapping("/{id}/comments")
    public ResponseEntity<CommentResponse> addComment(@PathVariable Long id, @Valid @RequestBody CommentRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(taskService.addComment(id, request, userService.getById(currentUser.id())));
    }
}
