package com.revature.P1BackEnd.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Reimbursement{
    @Id
    private String reimbursementId;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "employeeId")
    private Employee employee;
    @NotEmpty(message = "Description cannot be null")
    private String description;
    @NotNull(message = "Amount cannot be null")
    private double amount;
    @NotNull(message = "Status cannot be null")
    private Status status;
}
