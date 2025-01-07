package com.revature.P1BackEnd.model.dto;

import com.revature.P1BackEnd.model.Role;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;

public record EmployeeDTO(
        @NotEmpty(message = "Employee ID is required")
        String employeeId,
        @NotEmpty(message = "Name is required")
        String name,
        @NotEmpty(message = "Email is required")
        String email,
        @NotNull(message = "Role is required")
        Role role
) {
}
