package com.it_num.crud_api.dto.response;

import com.it_num.crud_api.domain.enums.ProjectStatus;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

public record ProjectResponse(
        UUID id,
        String name,
        String description,
        ProjectStatus status,
        LocalDate startDate,
        LocalDate endDate,
        OwnerSummary owner,
        Instant createdAt,
        Instant updatedAt
) {

    public record OwnerSummary(
            UUID id,
            String username,
            String fullName
    ) { }
}