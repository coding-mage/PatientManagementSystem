package com.pm.analyticsservice.mapper;

import com.pm.analyticsservice.dto.AnalyticsEventRequestDTO;
import com.pm.analyticsservice.dto.AnalyticsEventResponseDTO;
import com.pm.analyticsservice.model.AnalyticsEvent;

public class AnalyticsEventMapper {

    public static AnalyticsEventResponseDTO toResponseDTO(AnalyticsEvent event) {
        AnalyticsEventResponseDTO dto = new AnalyticsEventResponseDTO();
        dto.setId(event.getId().toString());
        dto.setPatientId(event.getPatientId());
        dto.setEventType(event.getEventType());
        dto.setDetails(event.getDetails());
        dto.setCreatedAt(event.getCreatedAt());
        return dto;
    }

    public static AnalyticsEvent toEntity(AnalyticsEventRequestDTO request) {
        AnalyticsEvent event = new AnalyticsEvent();
        event.setPatientId(request.getPatientId());
        event.setEventType(request.getEventType());
        event.setDetails(request.getDetails());
        return event;
    }
}
