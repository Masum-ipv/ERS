package com.revature.P1BackEnd.model.mapper;

import com.revature.P1BackEnd.model.Employee;
import com.revature.P1BackEnd.model.dto.EmployeeDTO;
import org.springframework.stereotype.Component;

@Component
public class EmployeeDtoMapper extends AbstractMapper<Employee, EmployeeDTO> {

    @Override
    public Employee dtoToEntity(EmployeeDTO employeeDTO) {
        Employee employee = new Employee();
        employee.setEmployeeId(employeeDTO.employeeId());
        employee.setName(employeeDTO.name());
        employee.setEmail(employeeDTO.email());
        employee.setRole(employeeDTO.role());
        return employee;
    }

    @Override
    public EmployeeDTO entityToDto(Employee employee) {
        return new EmployeeDTO(employee.getEmployeeId(), employee.getName(), employee.getEmail(), employee.getRole());
    }
}
