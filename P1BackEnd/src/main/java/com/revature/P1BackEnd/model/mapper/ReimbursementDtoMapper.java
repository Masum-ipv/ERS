package com.revature.P1BackEnd.model.mapper;

import com.revature.P1BackEnd.model.Reimbursement;
import com.revature.P1BackEnd.model.dto.ReimbursementDTO;

public class ReimbursementDtoMapper extends AbstractMapper<Reimbursement, ReimbursementDTO> {

    @Override
    public Reimbursement dtoToEntity(ReimbursementDTO reimbursementDTO) {
        Reimbursement reimbursement = new Reimbursement();
        reimbursement.setAmount(reimbursementDTO.amount());
        reimbursement.setDescription(reimbursementDTO.description());
        return reimbursement;
    }

    @Override
    public ReimbursementDTO entityToDto(Reimbursement reimbursement) {
        return new ReimbursementDTO(reimbursement.getEmployee().getEmployeeId(), reimbursement.getAmount(), reimbursement.getDescription());
    }
}
