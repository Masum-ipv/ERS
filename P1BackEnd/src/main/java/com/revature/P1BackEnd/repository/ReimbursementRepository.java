package com.revature.P1BackEnd.repository;

import com.revature.P1BackEnd.model.Reimbursement;
import com.revature.P1BackEnd.model.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReimbursementRepository extends JpaRepository<Reimbursement, String> {
    List<Reimbursement> findByEmployee_EmployeeIdAndStatus(String employeeId, Status status);

    List<Reimbursement> findByStatus(Status status);

    List<Reimbursement> findByEmployee_EmployeeId(String employeeId);
}
