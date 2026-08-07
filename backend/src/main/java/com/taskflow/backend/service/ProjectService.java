package com.taskflow.backend.service;

import com.taskflow.backend.dto.request.ProjectRequest;
import com.taskflow.backend.dto.response.ProjectResponse;
import com.taskflow.backend.entity.Project;
import com.taskflow.backend.entity.User;
import com.taskflow.backend.exception.ResourceNotFoundException;
import com.taskflow.backend.repository.ProjectRepository;
import com.taskflow.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final ActivityService activityService;

    @Transactional
    public List<ProjectResponse> listAll() {
        return projectRepository.findAllByOrderByCreatedAtDesc().stream()
                .map(ProjectResponse::from)
                .collect(Collectors.toList());
    }

    @Transactional
    public ProjectResponse getOne(Long id) {
        return ProjectResponse.from(findEntity(id));
    }

    public Project findEntity(Long id) {
        return projectRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found: " + id));
    }

    @Transactional
    public ProjectResponse create(ProjectRequest request, User creator) {
        Project project = Project.builder()
                .name(request.getName())
                .description(request.getDesc())
                .dueDate(request.getDue())
                .status(mapStatus(request.getStatus()))
                .progress(request.getProgress() != null ? request.getProgress() : 0)
                .createdBy(creator)
                .team(resolveTeam(request.getTeamUserIds()))
                .build();
        project.getTeam().add(creator);
        project = projectRepository.save(project);
        activityService.log("\uD83D\uDCC1", "New project created: " + project.getName(), creator);
        return ProjectResponse.from(project);
    }

    @Transactional
    public ProjectResponse update(Long id, ProjectRequest request, User actor) {
        Project project = findEntity(id);
        project.setName(request.getName());
        project.setDescription(request.getDesc());
        project.setDueDate(request.getDue());
        if (request.getStatus() != null) project.setStatus(mapStatus(request.getStatus()));
        if (request.getProgress() != null) project.setProgress(request.getProgress());
        if (request.getTeamUserIds() != null) project.setTeam(resolveTeam(request.getTeamUserIds()));
        project = projectRepository.save(project);
        activityService.log("\uD83D\uDCC1", "Project \"" + project.getName() + "\" updated", actor);
        return ProjectResponse.from(project);
    }

    @Transactional
    public void delete(Long id) {
        Project project = findEntity(id);
        projectRepository.delete(project);
    }

    @Transactional
    public ProjectResponse addMember(Long projectId, Long userId) {
        Project project = findEntity(projectId);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + userId));
        project.getTeam().add(user);
        return ProjectResponse.from(projectRepository.save(project));
    }

    private Set<User> resolveTeam(List<Long> ids) {
        Set<User> team = new HashSet<>();
        if (ids != null) {
            ids.forEach(id -> userRepository.findById(id).ifPresent(team::add));
        }
        return team;
    }

    private Project.ProjectStatus mapStatus(String status) {
        if (status == null) return Project.ProjectStatus.ACTIVE;
        return switch (status.trim().toLowerCase()) {
            case "on hold", "on_hold" -> Project.ProjectStatus.ON_HOLD;
            case "completed" -> Project.ProjectStatus.COMPLETED;
            default -> Project.ProjectStatus.ACTIVE;
        };
    }
}
