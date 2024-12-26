package com.revature.P1BackEnd.model.dto;

public record ReimbursementDTO(
        String employeeId,
        Double amount,
        String description
) {
}
