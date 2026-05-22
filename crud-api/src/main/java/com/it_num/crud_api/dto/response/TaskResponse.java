package com.it_num.crud_api.dto.response;

import com.it_num.crud_api.domain.enums.Priority;
import com.it_num.crud_api.domain.enums.TaskStatus;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Set;
import java.util.UUID;

public record TaskResponse(
        UUID id,
        String title,
        String description,
        TaskStatus status,
        Priority priority,
        LocalDate dueDate,
        ProjectSummary project,
        Set<TagSummary> tags,
        Instant createdAt,
        Instant updatedAt
) {

    /**
     * Lightweight nested record exposing essential project information.
     */
    public record ProjectSummary(
            UUID id,
            String name
    ) { }

    /**
     * Lightweight nested record exposing essential tag information.
     */
    public record TagSummary(
            UUID id,
            String name,
            String color
    ) { }
}