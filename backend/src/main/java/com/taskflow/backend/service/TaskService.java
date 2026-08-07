package com.taskflow.backend.service;

import com.taskflow.backend.dto.request.CommentRequest;
import com.taskflow.backend.dto.request.SubtaskRequest;
import com.taskflow.backend.dto.request.TaskRequest;
import com.taskflow.backend.dto.response.CommentResponse;
import com.taskflow.backend.dto.response.SubtaskResponse;
import com.taskflow.backend.dto.response.TaskResponse;
import com.taskflow.backend.entity.*;
import com.taskflow.backend.exception.ResourceNotFoundException;
import com.taskflow.backend.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final SubtaskRepository subtaskRepository;
    private final CommentRepository commentRepository;
    private final ActivityService activityService;
    private final NotificationService notificationService;

    @Transactional
    public List<TaskResponse> listAll(Long projectId, String status, String priority, Long assigneeId, String search) {
        return taskRepository.findAll().stream()
                .filter(t -> projectId == null || (t.getProject() != null && t.getProject().getId().equals(projectId)))
                .filter(t -> status == null || TaskResponse.humanStatus(t.getStatus().name()).equalsIgnoreCase(status))
                .filter(t -> priority == null || TaskResponse.humanPriority(t.getPriority().name()).equalsIgnoreCase(priority))
                .filter(t -> assigneeId == null || (t.getAssignee() != null && t.getAssignee().getId().equals(assigneeId)))
                .filter(t -> search == null || t.getName().toLowerCase().contains(search.toLowerCase()))
                .map(TaskResponse::from)
                .collect(Collectors.toList());
    }

    @Transactional
    public Map<String, List<TaskResponse>> kanbanBoard() {
        Map<String, List<TaskResponse>> board = new LinkedHashMap<>();
        for (Task.TaskStatus status : Task.TaskStatus.values()) {
            List<TaskResponse> tasks = taskRepository.findByStatus(status).stream()
                    .map(TaskResponse::from)
                    .collect(Collectors.toList());
            board.put(TaskResponse.humanStatus(status.name()), tasks);
        }
        return board;
    }

    @Transactional
    public TaskResponse getOne(Long id) {
        return TaskResponse.from(findEntity(id));
    }

    public Task findEntity(Long id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found: " + id));
    }

    @Transactional
    public TaskResponse create(TaskRequest request, User creator) {
        Project project = projectRepository.findById(request.getProjectId())
                .orElseThrow(() -> new ResourceNotFoundException("Project not found: " + request.getProjectId()));

        User assignee = null;
        if (request.getAssigneeId() != null) {
            assignee = userRepository.findById(request.getAssigneeId())
                    .orElseThrow(() -> new ResourceNotFoundException("User not found: " + request.getAssigneeId()));
        }

        Task task = Task.builder()
                .name(request.getName())
                .description(request.getDesc())
                .project(project)
                .assignee(assignee)
                .createdBy(creator)
                .dueDate(request.getDue())
                .status(mapStatus(request.getStatus()))
                .priority(mapPriority(request.getPriority()))
                .build();
        task = taskRepository.save(task);

        activityService.log("\u2795", "New task created: " + task.getName(), creator);
        if (assignee != null) {
            notificationService.notify(assignee, "\uD83D\uDCDD",
                    "Task \"" + task.getName() + "\" has been assigned to you.");
        }
        return TaskResponse.from(task);
    }

    @Transactional
    public TaskResponse update(Long id, TaskRequest request, User actor) {
        Task task = findEntity(id);
        task.setName(request.getName());
        task.setDescription(request.getDesc());
        if (request.getProjectId() != null) {
            task.setProject(projectRepository.findById(request.getProjectId())
                    .orElseThrow(() -> new ResourceNotFoundException("Project not found")));
        }
        Long previousAssigneeId = task.getAssignee() != null ? task.getAssignee().getId() : null;
        if (request.getAssigneeId() != null) {
            User assignee = userRepository.findById(request.getAssigneeId())
                    .orElseThrow(() -> new ResourceNotFoundException("User not found"));
            task.setAssignee(assignee);
            if (previousAssigneeId == null || !previousAssigneeId.equals(assignee.getId())) {
                notificationService.notify(assignee, "\uD83D\uDCDD",
                        "Task \"" + task.getName() + "\" has been assigned to you.");
            }
        }
        task.setDueDate(request.getDue());
        if (request.getStatus() != null) task.setStatus(mapStatus(request.getStatus()));
        if (request.getPriority() != null) task.setPriority(mapPriority(request.getPriority()));
        task = taskRepository.save(task);

        if (task.getStatus() == Task.TaskStatus.COMPLETED && task.getAssignee() != null) {
            notificationService.notify(task.getAssignee(), "\u2705",
                    "Task \"" + task.getName() + "\" has been completed.");
        }
        return TaskResponse.from(task);
    }

    @Transactional
    public TaskResponse updateStatus(Long id, String status, User actor) {
        Task task = findEntity(id);
        task.setStatus(mapStatus(status));
        task = taskRepository.save(task);
        activityService.log("\uD83D\uDD04", "Task \"" + task.getName() + "\" moved to " + TaskResponse.humanStatus(task.getStatus().name()), actor);
        return TaskResponse.from(task);
    }

    @Transactional
    public void delete(Long id) {
        Task task = findEntity(id);
        taskRepository.delete(task);
    }

    // ---------- Subtasks ----------
    @Transactional
    public List<SubtaskResponse> listSubtasks(Long taskId) {
        return subtaskRepository.findByTaskIdOrderByIdAsc(taskId).stream()
                .map(SubtaskResponse::from).collect(Collectors.toList());
    }

    @Transactional
    public SubtaskResponse addSubtask(Long taskId, SubtaskRequest request) {
        Task task = findEntity(taskId);
        Subtask subtask = Subtask.builder().title(request.getTitle()).task(task).build();
        return SubtaskResponse.from(subtaskRepository.save(subtask));
    }

    @Transactional
    public SubtaskResponse toggleSubtask(Long subtaskId) {
        Subtask subtask = subtaskRepository.findById(subtaskId)
                .orElseThrow(() -> new ResourceNotFoundException("Subtask not found: " + subtaskId));
        subtask.setCompleted(!subtask.isCompleted());
        return SubtaskResponse.from(subtaskRepository.save(subtask));
    }

    @Transactional
    public void deleteSubtask(Long subtaskId) {
        subtaskRepository.deleteById(subtaskId);
    }

    // ---------- Comments ----------
    @Transactional
    public List<CommentResponse> listComments(Long taskId) {
        return commentRepository.findByTaskIdOrderByCreatedAtAsc(taskId).stream()
                .map(CommentResponse::from).collect(Collectors.toList());
    }

    @Transactional
    public CommentResponse addComment(Long taskId, CommentRequest request, User author) {
        Task task = findEntity(taskId);
        Comment comment = Comment.builder().task(task).user(author).text(request.getText()).build();
        comment = commentRepository.save(comment);

        if (task.getAssignee() != null && !task.getAssignee().getId().equals(author.getId())) {
            notificationService.notify(task.getAssignee(), "\uD83D\uDCAC",
                    author.getFullName() + " commented on your task.");
        }
        return CommentResponse.from(comment);
    }

    // ---------- helpers ----------
    public List<Task> upcomingDeadlines(int days) {
        LocalDate now = LocalDate.now();
        return taskRepository.findByDueDateBetweenAndStatusNot(now, now.plusDays(days), Task.TaskStatus.COMPLETED);
    }

    private Task.TaskStatus mapStatus(String status) {
        if (status == null) return Task.TaskStatus.TODO;
        return switch (status.trim().toLowerCase()) {
            case "in progress", "in_progress" -> Task.TaskStatus.IN_PROGRESS;
            case "review" -> Task.TaskStatus.REVIEW;
            case "completed" -> Task.TaskStatus.COMPLETED;
            default -> Task.TaskStatus.TODO;
        };
    }

    private Task.TaskPriority mapPriority(String priority) {
        if (priority == null) return Task.TaskPriority.MEDIUM;
        return switch (priority.trim().toLowerCase()) {
            case "high" -> Task.TaskPriority.HIGH;
            case "low" -> Task.TaskPriority.LOW;
            default -> Task.TaskPriority.MEDIUM;
        };
    }
}
