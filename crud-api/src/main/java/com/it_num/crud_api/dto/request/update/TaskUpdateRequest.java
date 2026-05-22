package com.it_num.crud_api.dto.request.update;

import com.it_num.crud_api.domain.enums.Priority;
import com.it_num.crud_api.domain.enums.TaskStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.util.Set;
import java.util.UUID;

public record TaskUpdateRequest(

        @NotBlank(message = "Task title is required")
        @Size(min = 2, max = 200, message = "Task title must be between 2 and 200 characters")
        String title,

        @Size(max = 5000, message = "Description cannot exceed 5000 characters")
        String description,

        TaskStatus status,

        Priority priority,

        LocalDate dueDate,

        Set<UUID> tagIds

) { }