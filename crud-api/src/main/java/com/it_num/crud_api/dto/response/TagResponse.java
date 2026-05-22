package com.it_num.crud_api.dto.response;

import java.time.Instant;
import java.util.UUID;

public record TagResponse(
        UUID id,
        String name,
        String color,
        long taskCount,
        Instant createdAt
) { }