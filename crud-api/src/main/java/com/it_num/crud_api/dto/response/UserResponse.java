package com.it_num.crud_api.dto.response;

import java.time.Instant;
import java.util.UUID;

public record UserResponse(
        UUID id,
        String firstName,
        String lastName,
        String fullName,
        String username,
        String email,
        String bio,
        String avatarUrl,
        Instant createdAt,
        Instant updatedAt
) { }