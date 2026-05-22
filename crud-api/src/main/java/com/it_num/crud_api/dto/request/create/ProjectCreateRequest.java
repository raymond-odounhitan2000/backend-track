package com.it_num.crud_api.dto.request.create;

import com.it_num.crud_api.domain.enums.ProjectStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.util.UUID;

public record ProjectCreateRequest(

        @NotBlank(message = "Project name is required")
        @Size(min = 2, max = 150, message = "Project name must be between 2 and 150 characters")
        String name,

        @Size(max = 5000, message = "Description cannot exceed 5000 characters")
        String description,

        @NotNull(message = "Owner is required")
        UUID ownerId,

        ProjectStatus status,

        LocalDate startDate,

        LocalDate endDate

) { }