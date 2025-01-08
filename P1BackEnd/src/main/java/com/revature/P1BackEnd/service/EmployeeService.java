package com.revature.P1BackEnd.service;

import com.revature.P1BackEnd.model.ApiResponse;
import com.revature.P1BackEnd.model.Employee;
import com.revature.P1BackEnd.model.Reimbursement;
import com.revature.P1BackEnd.model.dto.EmployeeDTO;
import com.revature.P1BackEnd.model.dto.LoginRequestDTO;
import com.revature.P1BackEnd.model.mapper.EmployeeDtoMapper;
import com.revature.P1BackEnd.repository.EmployeeRepository;
import com.revature.P1BackEnd.utils.JwtUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
public class EmployeeService {
    private final EmployeeRepository employeeRepository;
    private final EmployeeDtoMapper EmployeeDtoMapper;
    private final JwtUtils jwtUtils;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final RabbitTemplate rabbitTemplate;
    private final String exchangeName;
    private final Logger logger = LoggerFactory.getLogger(EmployeeService.class);

    public EmployeeService(EmployeeRepository employeeRepository, EmployeeDtoMapper employeeDtoMapper,
                           JwtUtils jwtUtils, AuthenticationManager authenticationManager, PasswordEncoder passwordEncoder,
                           RabbitTemplate rabbitTemplate, @Value("${rabbitmq.exchange.name}") String exchangeName) {
        this.employeeRepository = employeeRepository;
        this.EmployeeDtoMapper = employeeDtoMapper;
        this.jwtUtils = jwtUtils;
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
        this.rabbitTemplate = rabbitTemplate;
        this.exchangeName = exchangeName;
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
    public ApiResponse registerEmployee(Employee employee) {
        logger.info("Inserting employee: {}", employee.getName());
        employee.setEmployeeId(UUID.randomUUID().toString());
        if (employeeRepository.existsByEmail(employee.getEmail())) {
            throw new RuntimeException("Employee already exists with email: " + employee.getEmail());
        }
        employee.setPassword(passwordEncoder.encode(employee.getPassword()));
        Employee savedEmployee = employeeRepository.save(employee);
        savedEmployee.setPassword(null);
        ApiResponse response = new ApiResponse("Employee inserted successfully", savedEmployee);

        // Set the message properties
        MessageProperties messageProperties = new MessageProperties();
        messageProperties.setHeader("email-address", savedEmployee.getEmail());
        messageProperties.setHeader("phone-number", "+1234567890");

        // Create a message
        String messageBody = "Welcome to the ERS, " + savedEmployee.getName() + ", your account has been successfully registered";
        Message message = new Message(messageBody.getBytes(), messageProperties);

        // Publish the message
        rabbitTemplate.convertAndSend(exchangeName, "registration.email", message);
        rabbitTemplate.convertAndSend(exchangeName, "registration.sms", message);


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

        Authentication authenticationRequest =
                UsernamePasswordAuthenticationToken.unauthenticated(employee.email(), employee.password());
        Authentication authenticationResponse =
                authenticationManager.authenticate(authenticationRequest);
        logger.debug("Authentication response: {}", authenticationResponse);
        if (authenticationResponse == null) {
            throw new RuntimeException("Invalid email or password");
        }

        final Employee loggedInUser = (Employee) authenticationResponse.getPrincipal();

        // Extract roles from the authenticated user
        String role = loggedInUser.getRole().name();

        final String token = jwtUtils.generateJwtToken(loggedInUser.getName(), role);

        logger.debug("Logged in user: {} with token: {}", loggedInUser.getName(), token);

        ApiResponse response = new ApiResponse("Employee logged in successfully", Map.of(
                "userId", loggedInUser.getEmployeeId(), "token", token));
        return ResponseEntity.ok(response);
    }
}
