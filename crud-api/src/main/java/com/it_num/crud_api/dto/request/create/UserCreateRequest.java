package com.it_num.crud_api.dto.request.create;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UserCreateRequest(

        @NotBlank(message = "First name is required")
        @Size(min = 2, max = 50, message = "First name must be between 2 and 50 characters")
        String firstName,

        @NotBlank(message = "Last name is required")
        @Size(min = 2, max = 50, message = "Last name must be between 2 and 50 characters")
        String lastName,

        @NotBlank(message = "Username is required")
        @Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters")
        @Pattern(
                regexp = "^[a-zA-Z0-9_.-]+$",
                message = "Username can only contain letters, digits, underscores, dots and hyphens"
        )
        String username,

        @NotBlank(message = "Email is required")
        @Email(message = "Invalid email format")
        @Size(max = 255, message = "Email cannot exceed 255 characters")
        String email,

        @Size(max = 1000, message = "Bio cannot exceed 1000 characters")
        String bio,

        @Size(max = 500, message = "Avatar URL cannot exceed 500 characters")
        @Pattern(
                regexp = "^(https?://.*)?$",
                message = "Avatar URL must start with http:// or https://"
        )
        String avatarUrl

) { }