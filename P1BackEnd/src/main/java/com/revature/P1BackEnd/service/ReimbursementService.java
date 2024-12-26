package com.revature.P1BackEnd.service;

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
        return ResponseEntity.ok(reimbursements);
    }

    public ResponseEntity<?> getReimbursementById(String id) {
        logger.info("Retrieving reimbursement with id: {}", id);
        Optional<Reimbursement> reimbursement = reimbursementRepository.findById(id);
        if (reimbursement.isEmpty()) {
            throw new RuntimeException("Reimbursement not found with id: " + id);
        }
        return ResponseEntity.ok(reimbursement.get());
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

        Reimbursement reimbursement = new Reimbursement(
                UUID.randomUUID().toString(),
                employee.get(),
                reimbursementDTO.description(),
                reimbursementDTO.amount(),
                Status.PENDING
        );
        return ResponseEntity.ok(reimbursementRepository.save(reimbursement));
    }

    public ResponseEntity<?> updateReimbursement(Reimbursement reimbursement) {
        logger.info("Updating reimbursement: {}", reimbursement);
        Optional<Reimbursement> existingReimbursement = reimbursementRepository.findById(reimbursement.getReimbursementId());
        if (existingReimbursement.isEmpty()) {
            throw new RuntimeException("Reimbursement not found with id: " + reimbursement.getReimbursementId());
        }
        reimbursement.setEmployee(existingReimbursement.get().getEmployee());
        return ResponseEntity.ok(reimbursementRepository.save(reimbursement));
    }

    public ResponseEntity<?> deleteReimbursement(String id) {
        logger.info("Deleting reimbursement with id: {}", id);
        if (!reimbursementRepository.existsById(id)) {
            throw new RuntimeException("Reimbursement not found with id: " + id);
        }
        reimbursementRepository.deleteById(id);
        return ResponseEntity.ok("Reimbursement deleted successfully");
    }

    public ResponseEntity<?> getPendingReimbursementsByEmployee(Status status, String employeeId) {
        logger.info("Retrieving pending reimbursements for employee with id: {}", employeeId);
        List<Reimbursement> reimbursements = reimbursementRepository.findByEmployee_EmployeeIdAndStatus(employeeId, status);
        return ResponseEntity.ok(reimbursements);
    }

    public ResponseEntity<?> getPendingReimbursements() {
        logger.info("Retrieving all pending reimbursements");
        List<Reimbursement> reimbursements = reimbursementRepository.findByStatus(Status.PENDING);
        return ResponseEntity.ok(reimbursements);
    }

    public ResponseEntity<?> getReimbursementsByEmployee(String employeeId) {
        logger.info("Retrieving reimbursements for employee with id: {}", employeeId);
        List<Reimbursement> reimbursements = reimbursementRepository.findByEmployee_EmployeeId(employeeId);
        return ResponseEntity.ok(reimbursements);
    }
}
