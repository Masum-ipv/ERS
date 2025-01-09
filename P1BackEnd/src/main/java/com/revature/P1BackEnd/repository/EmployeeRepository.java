package com.revature.P1BackEnd.repository;

import com.revature.P1BackEnd.model.Employee;
import com.revature.P1BackEnd.model.Reimbursement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, UUID> {
    boolean existsByEmail(String email);

    Employee findByEmailAndPassword(String email, String password);

    List<Reimbursement> findByEmployeeId(UUID employeeId);

    Optional<Employee> findByEmail(String email);
}
