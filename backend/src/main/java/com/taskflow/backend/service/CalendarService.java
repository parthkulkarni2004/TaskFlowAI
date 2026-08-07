package com.taskflow.backend.service;

import com.taskflow.backend.dto.request.CalendarEventRequest;
import com.taskflow.backend.dto.response.CalendarEventResponse;
import com.taskflow.backend.entity.CalendarEvent;
import com.taskflow.backend.entity.Project;
import com.taskflow.backend.entity.User;
import com.taskflow.backend.repository.CalendarEventRepository;
import com.taskflow.backend.repository.ProjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CalendarService {

    private final CalendarEventRepository calendarEventRepository;
    private final ProjectRepository projectRepository;

    @Transactional
    public List<CalendarEventResponse> forMonth(int year, int month) {
        LocalDate start = LocalDate.of(year, month, 1);
        LocalDate end = start.withDayOfMonth(start.lengthOfMonth());
        return calendarEventRepository.findByEventDateBetween(start, end).stream()
                .map(CalendarEventResponse::from)
                .collect(Collectors.toList());
    }

    @Transactional
    public CalendarEventResponse create(CalendarEventRequest request, User creator) {
        Project project = null;
        if (request.getProjectId() != null) {
            project = projectRepository.findById(request.getProjectId()).orElse(null);
        }
        CalendarEvent event = CalendarEvent.builder()
                .label(request.getLabel())
                .eventDate(request.getDate())
                .color(request.getColor() != null ? request.getColor() : "blue")
                .project(project)
                .createdBy(creator)
                .build();
        return CalendarEventResponse.from(calendarEventRepository.save(event));
    }

    @Transactional
    public void delete(Long id) {
        calendarEventRepository.deleteById(id);
    }
}
