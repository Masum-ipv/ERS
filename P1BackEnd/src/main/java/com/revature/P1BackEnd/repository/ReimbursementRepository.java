package com.revature.P1BackEnd.repository;

import com.revature.P1BackEnd.model.Reimbursement;
import com.revature.P1BackEnd.model.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ReimbursementRepository extends JpaRepository<Reimbursement, UUID> {
    List<Reimbursement> findByEmployee_EmployeeIdAndStatus(UUID employeeId, Status status);

    List<Reimbursement> findByStatus(Status status);

    List<Reimbursement> findByEmployee_EmployeeId(UUID employeeId);
}
