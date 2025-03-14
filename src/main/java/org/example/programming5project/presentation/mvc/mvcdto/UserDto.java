package org.example.programming5project.presentation.mvc.mvcdto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record UserDto(
        @NotBlank(message = "Username is required")
        String username,
        @Min(value = 4, message = "Password must be at least 4 characters")
                      String password) {
}

