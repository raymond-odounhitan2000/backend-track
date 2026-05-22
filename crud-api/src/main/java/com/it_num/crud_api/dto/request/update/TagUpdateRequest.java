package com.it_num.crud_api.dto.request.update;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record TagUpdateRequest(

        @NotBlank(message = "Tag name is required")
        @Size(min = 2, max = 50, message = "Tag name must be between 2 and 50 characters")
        String name,

        @Pattern(
                regexp = "^(#[0-9A-Fa-f]{6})?$",
                message = "Color must be a valid hex code (e.g. #FF5733) or empty"
        )
        String color

) { }