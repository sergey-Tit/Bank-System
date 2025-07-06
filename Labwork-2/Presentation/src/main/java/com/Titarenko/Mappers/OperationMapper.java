package com.Titarenko.Mappers;

import com.Titarenko.DTO.BankAccountDTO;
import com.Titarenko.DTO.OperationDTO;
import Models.Operation;

public class OperationMapper {
    public static OperationDTO toDTO(Operation operation) {
        return new OperationDTO(
                operation.getId(),
                operation.getType(),
                operation.getAmount(),
                BankAccountMapper.toDTO(operation.getHomeAccount()),
                BankAccountMapper.toDTO(operation.getTargetAccount()),
                operation.getDate()
        );
    }
}
