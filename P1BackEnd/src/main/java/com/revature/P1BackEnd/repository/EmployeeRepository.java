package com.revature.P1BackEnd.repository;

import com.revature.P1BackEnd.model.Employee;
import com.revature.P1BackEnd.model.Reimbursement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, String> {
    boolean existsByEmail(String email);

    Employee findByEmail(String email);

    List<Reimbursement> findByEmployeeId(String employeeId);
}
