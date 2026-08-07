package com.taskflow.backend.repository;

import com.taskflow.backend.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.time.LocalDate;
import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long>, JpaSpecificationExecutor<Task> {
    List<Task> findByProjectId(Long projectId);
    List<Task> findByAssignee_Id(Long userId);
    List<Task> findByStatus(Task.TaskStatus status);
    List<Task> findByDueDateBetweenAndStatusNot(LocalDate start, LocalDate end, Task.TaskStatus status);
    long countByStatus(Task.TaskStatus status);
}
