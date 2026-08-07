package com.taskflow.backend.controller;

import com.taskflow.backend.dto.request.CalendarEventRequest;
import com.taskflow.backend.dto.response.CalendarEventResponse;
import com.taskflow.backend.security.CurrentUser;
import com.taskflow.backend.service.CalendarService;
import com.taskflow.backend.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/calendar")
@RequiredArgsConstructor
public class CalendarController {

    private final CalendarService calendarService;
    private final UserService userService;
    private final CurrentUser currentUser;

    @GetMapping("/events")
    public ResponseEntity<List<CalendarEventResponse>> events(
            @RequestParam(required = false) Integer year,
            @RequestParam(required = false) Integer month) {
        LocalDate now = LocalDate.now();
        int y = year != null ? year : now.getYear();
        int m = month != null ? month : now.getMonthValue();
        return ResponseEntity.ok(calendarService.forMonth(y, m));
    }

    @PostMapping("/events")
    public ResponseEntity<CalendarEventResponse> create(@Valid @RequestBody CalendarEventRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(calendarService.create(request, userService.getById(currentUser.id())));
    }

    @DeleteMapping("/events/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        calendarService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
