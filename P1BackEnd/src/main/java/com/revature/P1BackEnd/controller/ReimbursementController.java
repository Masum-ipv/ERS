package com.revature.P1BackEnd.controller;

import com.revature.P1BackEnd.model.Reimbursement;
import com.revature.P1BackEnd.model.Status;
import com.revature.P1BackEnd.model.dto.ReimbursementDTO;
import com.revature.P1BackEnd.service.ReimbursementService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/reimbursement")
public class ReimbursementController {
    private final ReimbursementService reimbursementService;

    public ReimbursementController(ReimbursementService reimbursementService) {
        this.reimbursementService = reimbursementService;
    }

    @GetMapping("")
    public ResponseEntity<?> getReimbursements() {
        return reimbursementService.getReimbursements();
    }

    @GetMapping("/employee/{employeeId}")
    public ResponseEntity<?> getReimbursementsByEmployee(@PathVariable UUID employeeId) {
        return reimbursementService.getReimbursementsByEmployee(employeeId);
    }

    @GetMapping("/PENDING")
    public ResponseEntity<?> getPendingReimbursements() {
        return reimbursementService.getPendingReimbursements();
    }

    @GetMapping("/{status}/{employeeId}")
    public ResponseEntity<?> getPendingReimbursementsByEmployee(@PathVariable Status status, @PathVariable UUID employeeId) {
        return reimbursementService.getPendingReimbursementsByEmployee(status, employeeId);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getReimbursementById(@PathVariable UUID id) {
        return reimbursementService.getReimbursementById(id);
    }

    @PostMapping
    public ResponseEntity<?> insertReimbursement(@Valid @RequestBody ReimbursementDTO reimbursementDTO) {
        return reimbursementService.insertReimbursement(reimbursementDTO);
    }

    @PutMapping("")
    public ResponseEntity<?> updateReimbursement(@Valid @RequestBody Reimbursement reimbursement) {
        return reimbursementService.updateReimbursement(reimbursement);
    }

    @DeleteMapping("/{reimbursementId}")
    public ResponseEntity<?> deleteReimbursement(@PathVariable UUID reimbursementId) {
        return reimbursementService.deleteReimbursement(reimbursementId);
    }
}
