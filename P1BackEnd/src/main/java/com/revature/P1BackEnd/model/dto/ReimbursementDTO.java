package com.revature.P1BackEnd.model.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record ReimbursementDTO(
        @NotEmpty(message = "Employee ID is required")
        String employeeId,
        @NotNull(message = "Amount is required")
        Double amount,
        @NotEmpty(message = "Description is required")
        String description
) {
}
