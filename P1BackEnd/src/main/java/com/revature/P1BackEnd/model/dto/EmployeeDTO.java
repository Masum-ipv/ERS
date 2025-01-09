package com.revature.P1BackEnd.model.dto;

import com.revature.P1BackEnd.model.Role;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;
import java.util.UUID;

public record EmployeeDTO(
        @NotNull(message = "Employee ID is required")
        UUID employeeId,
        @NotEmpty(message = "Name is required")
        String name,
        @NotEmpty(message = "Email is required")
        String email,
        @NotNull(message = "Role is required")
        Role role
) implements Serializable {
    private static final long serialVersionUID = 1L;
}
