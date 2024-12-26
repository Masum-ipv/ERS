package com.revature.P1BackEnd.controller;

import com.revature.P1BackEnd.model.Employee;
import com.revature.P1BackEnd.model.dto.LoginRequestDTO;
import com.revature.P1BackEnd.service.EmployeeService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/employee")
public class EmployeeController {
    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @GetMapping("")
    public ResponseEntity<?> getEmployee() {
        return employeeService.getEmployee();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getEmployeeById(@PathVariable String id) {
        return employeeService.getEmployeeById(id);
    }

    @GetMapping("/reimbursements/{id}")
    public ResponseEntity<?> getReimbursementsByEmployee(@PathVariable String id) {
        return employeeService.getAllReimbursementsByEmployee(id);
    }

    @PostMapping
    public ResponseEntity<?> insertEmployee(@Valid @RequestBody Employee employee) {
        return employeeService.insertEmployee(employee);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDTO employee) {
        return employeeService.login(employee);
    }

    @PutMapping
    public ResponseEntity<?> updateEmployee(@Valid @RequestBody Employee employee) {
        return employeeService.updateEmployee(employee);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteEmployee(@PathVariable String id) {
        return employeeService.deleteEmployee(id);
    }


}
