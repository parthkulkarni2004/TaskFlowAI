package com.taskflow.backend.dto.response;

import com.taskflow.backend.entity.CalendarEvent;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CalendarEventResponse {
    private Long id;
    private String label;
    private LocalDate date;
    private String color;

    public static CalendarEventResponse from(CalendarEvent e) {
        return CalendarEventResponse.builder()
                .id(e.getId())
                .label(e.getLabel())
                .date(e.getEventDate())
                .color(e.getColor())
                .build();
    }
}
