package com.taskflow.backend.repository;

import com.taskflow.backend.entity.Subtask;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SubtaskRepository extends JpaRepository<Subtask, Long> {
    List<Subtask> findByTaskIdOrderByIdAsc(Long taskId);
}
