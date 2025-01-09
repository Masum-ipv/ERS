package com.revature.P1BackEnd.model.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record ReimbursementDTO(
        @NotNull(message = "Employee ID is required")
        UUID employeeId,
        @NotNull(message = "Amount is required")
        Double amount,
        @NotEmpty(message = "Description is required")
        String description
) {
}
