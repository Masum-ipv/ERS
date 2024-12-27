package com.revature.P1BackEnd.service;

import com.revature.P1BackEnd.model.Employee;
import com.revature.P1BackEnd.model.Reimbursement;
import com.revature.P1BackEnd.model.dto.EmployeeDTO;
import com.revature.P1BackEnd.model.dto.LoginRequestDTO;
import com.revature.P1BackEnd.repository.EmployeeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class EmployeeService {
    private final EmployeeRepository employeeRepository;
    private final Logger logger = LoggerFactory.getLogger(EmployeeService.class);

    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    public ResponseEntity<?> getEmployee() {
        List<Employee> employees = employeeRepository.findAll();
        logger.info("Retrieving all employees, Employee count: {}", employees.size());
        List<EmployeeDTO> employeeDTOs = employees.stream().map(employee -> new EmployeeDTO(employee.getEmployeeId(), employee.getName(), employee.getEmail(), employee.getRole())).toList();
        return ResponseEntity.ok(employeeDTOs);
    }

    public ResponseEntity<?> getEmployeeById(String id) {
        logger.info("Retrieving employee with id: {}", id);
        Optional<Employee> employee = employeeRepository.findById(id);
        if (employee.isEmpty()) {
            throw new RuntimeException("Employee not found with id: " + id);
        }
        EmployeeDTO employeeDTO = new EmployeeDTO(
                employee.get().getEmployeeId(), employee.get().getName(),
                employee.get().getEmail(), employee.get().getRole());
        return ResponseEntity.ok(employeeDTO);
    }


    public ResponseEntity<?> getAllReimbursementsByEmployee(String employeeId) {
        logger.info("Retrieving reimbursements for employee with id: {}", employeeId);
        List<Reimbursement> reimbursements = employeeRepository.findByEmployeeId(employeeId);
        return ResponseEntity.ok(reimbursements);
    }

    public ResponseEntity<?> insertEmployee(Employee employee) {
        logger.info("Inserting employee: {}", employee.getName());
        employee.setEmployeeId(UUID.randomUUID().toString());
        if (employeeRepository.existsByEmail(employee.getEmail())) {
            throw new IllegalArgumentException("Employee already exists with email: " + employee.getEmail());
        }
        Employee savedEmployee = employeeRepository.save(employee);
        return ResponseEntity.ok(savedEmployee);
    }

    public ResponseEntity<?> updateEmployee(EmployeeDTO employeeDTO) {
        logger.info("Updating employee: {}", employeeDTO.name());

        Optional<Employee> employee = employeeRepository.findById(employeeDTO.employeeId());
        if (employee.isEmpty()) {
            throw new RuntimeException("Employee not found with id: " + employeeDTO.employeeId());
        }

        employee.get().setName(employeeDTO.name());
        employee.get().setEmail(employeeDTO.email());
        employee.get().setRole(employeeDTO.role());

        employeeRepository.save(employee.get());
        return ResponseEntity.ok(employeeDTO);
    }

    public ResponseEntity<?> deleteEmployee(String id) {
        logger.info("Deleting employee with id: {}", id);
        if (!employeeRepository.existsById(id)) {
            throw new RuntimeException("Employee not found with id: " + id);
        }
        employeeRepository.deleteById(id);
        return ResponseEntity.ok("Employee deleted successfully");
    }

    public ResponseEntity<?> login(LoginRequestDTO employee) {
        logger.info("Logging in employee: {}", employee.email());

        Employee existingEmployee = employeeRepository.findByEmail(employee.email());

        if (existingEmployee != null && existingEmployee.getPassword().equals(employee.password())) {
            return ResponseEntity.ok(existingEmployee);
        } else {
            throw new RuntimeException("Invalid email or password");
        }
    }
}
