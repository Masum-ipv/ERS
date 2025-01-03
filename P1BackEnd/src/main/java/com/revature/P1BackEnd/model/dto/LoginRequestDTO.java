package com.revature.P1BackEnd.model.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public record LoginRequestDTO(
        @NotEmpty(message = "Email is required")
        String email,
        @Size(min = 8, message = "Password must be at least 8 characters long")
        String password
) {
}
