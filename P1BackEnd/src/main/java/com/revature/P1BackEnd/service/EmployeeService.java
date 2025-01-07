package com.revature.P1BackEnd.service;

import com.revature.P1BackEnd.model.ApiResponse;
import com.revature.P1BackEnd.model.Employee;
import com.revature.P1BackEnd.model.Reimbursement;
import com.revature.P1BackEnd.model.dto.EmployeeDTO;
import com.revature.P1BackEnd.model.dto.LoginRequestDTO;
import com.revature.P1BackEnd.model.mapper.EmployeeDtoMapper;
import com.revature.P1BackEnd.repository.EmployeeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class EmployeeService {
    private final EmployeeRepository employeeRepository;
    private final EmployeeDtoMapper EmployeeDtoMapper;
    private final Logger logger = LoggerFactory.getLogger(EmployeeService.class);

    public EmployeeService(EmployeeRepository employeeRepository, EmployeeDtoMapper employeeDtoMapper) {
        this.employeeRepository = employeeRepository;
        this.EmployeeDtoMapper = employeeDtoMapper;
    }

    @Cacheable(value = "employees")
    public ApiResponse getAllEmployee() {
        List<Employee> employees = employeeRepository.findAll();
        logger.info("Retrieving all employees, Employee count: {}", employees.size());
        List<EmployeeDTO> employeeDTOs = employees.stream().map(employee -> new EmployeeDTO(
                employee.getEmployeeId(), employee.getName(),
                employee.getEmail(), employee.getRole())).toList();
        ApiResponse response = new ApiResponse("Employees retrieved successfully", employeeDTOs);
        return response;
    }

    @Cacheable(value = "employeesById", key = "#id")
    public ApiResponse getEmployeeById(String id) {
        logger.info("Retrieving employee with id: {}", id);
        Optional<Employee> employee = employeeRepository.findById(id);
        if (employee.isEmpty()) {
            throw new RuntimeException("Employee not found with id: " + id);
        }
        EmployeeDTO employeeDTO = EmployeeDtoMapper.entityToDto(employee.get());
        ApiResponse response = new ApiResponse("Employee retrieved successfully", employeeDTO);

        return response;
    }


    public ResponseEntity<?> getAllReimbursementsByEmployee(String employeeId) {
        logger.info("Retrieving reimbursements for employee with id: {}", employeeId);
        List<Reimbursement> reimbursements = employeeRepository.findByEmployeeId(employeeId);
        ApiResponse response = new ApiResponse("Reimbursements retrieved successfully", reimbursements);
        return ResponseEntity.ok(response);
    }

    @CacheEvict(value = "employees", allEntries = true)
    public ApiResponse insertEmployee(Employee employee) {
        logger.info("Inserting employee: {}", employee.getName());
        employee.setEmployeeId(UUID.randomUUID().toString());
        if (employeeRepository.existsByEmail(employee.getEmail())) {
            throw new RuntimeException("Employee already exists with email: " + employee.getEmail());
        }
        Employee savedEmployee = employeeRepository.save(employee);
        savedEmployee.setPassword(null);
        ApiResponse response = new ApiResponse("Employee inserted successfully", savedEmployee);
        return response;
    }

    @Caching(evict = {
            @CacheEvict(value = "employees", allEntries = true),
            @CacheEvict(value = "employeesById", key = "#employeeDTO.employeeId")
    })
    public ApiResponse updateEmployee(EmployeeDTO employeeDTO) {
        logger.info("Updating employee: {}", employeeDTO.name());

        Optional<Employee> employee = employeeRepository.findById(employeeDTO.employeeId());
        if (employee.isEmpty()) {
            throw new RuntimeException("Employee not found with id: " + employeeDTO.employeeId());
        }

        employee.get().setName(employeeDTO.name());
        employee.get().setEmail(employeeDTO.email());
        employee.get().setRole(employeeDTO.role());
        employeeRepository.save(employee.get());

        ApiResponse response = new ApiResponse("Employee updated successfully", employeeDTO);
        return response;
    }

    @Caching(evict = {
            @CacheEvict(value = "employees", allEntries = true),
            @CacheEvict(value = "employeesById", key = "#id")
    })
    public ApiResponse deleteEmployee(String id) {
        logger.info("Deleting employee with id: {}", id);
        if (!employeeRepository.existsById(id)) {
            throw new RuntimeException("Employee not found with id: " + id);
        }
        employeeRepository.deleteById(id);
        ApiResponse response = new ApiResponse("Employee deleted successfully", null);
        return response;
    }

    public ResponseEntity<?> login(LoginRequestDTO employee) {
        logger.info("Logging in employee: {}", employee.email());

        Employee existingEmployee = employeeRepository.findByEmailAndPassword(employee.email(), employee.password());
        if (existingEmployee == null) {
            throw new RuntimeException("Invalid email or password");
        }
        existingEmployee.setPassword(null);
        ApiResponse response = new ApiResponse("Employee logged in successfully", existingEmployee);
        return ResponseEntity.ok(response);
    }
}
