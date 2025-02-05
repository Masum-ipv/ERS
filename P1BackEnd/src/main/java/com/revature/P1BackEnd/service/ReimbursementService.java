package com.revature.P1BackEnd.service;

import com.revature.P1BackEnd.model.ApiResponse;
import com.revature.P1BackEnd.model.Employee;
import com.revature.P1BackEnd.model.Reimbursement;
import com.revature.P1BackEnd.model.Status;
import com.revature.P1BackEnd.model.dto.ReimbursementDTO;
import com.revature.P1BackEnd.repository.EmployeeRepository;
import com.revature.P1BackEnd.repository.ReimbursementRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static java.util.stream.Collectors.toList;

@Service
public class ReimbursementService {
    private final ReimbursementRepository reimbursementRepository;
    private final EmployeeRepository employeeRepository;
    private final Logger logger = LoggerFactory.getLogger(ReimbursementService.class);

    public ReimbursementService(ReimbursementRepository reimbursementRepository, EmployeeRepository employeeRepository) {
        this.reimbursementRepository = reimbursementRepository;
        this.employeeRepository = employeeRepository;
    }

    public ResponseEntity<?> getReimbursements() {
        List<Reimbursement> reimbursements = reimbursementRepository.findAll();
        logger.info("Retrieving all reimbursements, Reimbursement count: {}", reimbursements.size());
        reimbursements = reimbursements.stream().map(this::getReimbursementWithOutPassword).collect(toList());

        ApiResponse response = new ApiResponse("Reimbursements retrieved successfully", reimbursements);
        return ResponseEntity.ok(response);
    }

    public ResponseEntity<?> getReimbursementById(UUID id) {
        logger.info("Retrieving reimbursement with id: {}", id);
        Optional<Reimbursement> reimbursement = reimbursementRepository.findById(id);
        if (reimbursement.isEmpty()) {
            throw new RuntimeException("Reimbursement not found with id: " + id);
        }
        reimbursement = Optional.of(getReimbursementWithOutPassword(reimbursement.get()));
        ApiResponse response = new ApiResponse("Reimbursement retrieved successfully", reimbursement.get());
        return ResponseEntity.ok(response);
    }

    public ResponseEntity<?> insertReimbursement(ReimbursementDTO reimbursementDTO) {
        logger.info("Inserting reimbursement: {}", reimbursementDTO.employeeId());
        if (reimbursementDTO.amount() == null || reimbursementDTO.amount() <= 0 || reimbursementDTO.description().isEmpty()) {
            throw new RuntimeException("Invalid reimbursement data");
        }

        Optional<Employee> employee = employeeRepository.findById(reimbursementDTO.employeeId());
        if (employee.isEmpty()) {
            throw new RuntimeException("Employee not found with id: " + reimbursementDTO.employeeId());
        }

        Reimbursement reimbursement = new Reimbursement();
        reimbursement.setEmployee(employee.get());
        reimbursement.setDescription(reimbursementDTO.description());
        reimbursement.setAmount(reimbursementDTO.amount());
        reimbursement.setStatus(Status.PENDING);

        Reimbursement savedReimbursement = reimbursementRepository.save(reimbursement);
        Reimbursement updatedReimbursement = getReimbursementWithOutPassword(savedReimbursement);
        ApiResponse response = new ApiResponse("Reimbursement inserted successfully", updatedReimbursement);

        return ResponseEntity.ok(response);
    }

    public ResponseEntity<?> updateReimbursement(Reimbursement reimbursement) {
        logger.info("Updating reimbursement: {}", reimbursement);
        Optional<Reimbursement> existingReimbursement = reimbursementRepository.findById(reimbursement.getReimbursementId());
        if (existingReimbursement.isEmpty()) {
            throw new RuntimeException("Reimbursement not found with id: " + reimbursement.getReimbursementId());
        }
        reimbursement.setEmployee(existingReimbursement.get().getEmployee());
        Reimbursement savedReimbursement = reimbursementRepository.save(reimbursement);
        Reimbursement updatedReimbursement = getReimbursementWithOutPassword(savedReimbursement);

        ApiResponse response = new ApiResponse("Reimbursement updated successfully", updatedReimbursement);
        return ResponseEntity.ok(response);
    }

    public ResponseEntity<?> deleteReimbursement(UUID reimbursementId) {
        logger.info("Deleting reimbursement with id: {}", reimbursementId);
        if (!reimbursementRepository.existsById(reimbursementId)) {
            throw new RuntimeException("Reimbursement not found with id: " + reimbursementId);
        }
        reimbursementRepository.deleteById(reimbursementId);
        ApiResponse response = new ApiResponse("Reimbursement deleted successfully", null);
        return ResponseEntity.ok(response);
    }

    public ResponseEntity<?> getPendingReimbursementsByEmployee(Status status, UUID employeeId) {
        logger.info("Retrieving pending reimbursements for employee with id: {}", employeeId);
        List<Reimbursement> reimbursements = reimbursementRepository.findByEmployee_EmployeeIdAndStatus(employeeId, status);
        reimbursements = reimbursements.stream().map(this::getReimbursementWithOutPassword).collect(toList());
        ApiResponse response = new ApiResponse("Reimbursements retrieved successfully", reimbursements);
        return ResponseEntity.ok(response);
    }

    public ResponseEntity<?> getPendingReimbursements() {
        logger.info("Retrieving all pending reimbursements");
        List<Reimbursement> reimbursements = reimbursementRepository.findByStatus(Status.PENDING);
        reimbursements = reimbursements.stream().map(this::getReimbursementWithOutPassword).collect(toList());
        ApiResponse response = new ApiResponse("Reimbursements retrieved successfully", reimbursements);
        return ResponseEntity.ok(response);
    }

    public ResponseEntity<?> getReimbursementsByEmployee(UUID employeeId) {
        logger.info("Retrieving reimbursements for employee with id: {}", employeeId);
        List<Reimbursement> reimbursements = reimbursementRepository.findByEmployee_EmployeeId(employeeId);
        reimbursements = reimbursements.stream().map(this::getReimbursementWithOutPassword).collect(toList());
        ApiResponse response = new ApiResponse("Reimbursements retrieved successfully", reimbursements);
        return ResponseEntity.ok(response);
    }

    private Reimbursement getReimbursementWithOutPassword(Reimbursement reimbursement) {
        reimbursement.getEmployee().setPassword(null);
        return new Reimbursement(
                reimbursement.getReimbursementId(),
                reimbursement.getEmployee(),
                reimbursement.getDescription(),
                reimbursement.getAmount(),
                reimbursement.getStatus()
        );
    }
}
