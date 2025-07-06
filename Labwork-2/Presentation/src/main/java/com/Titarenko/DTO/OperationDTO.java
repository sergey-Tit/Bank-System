package com.Titarenko.DTO;

import Models.OperationType;

import java.math.BigDecimal;
import java.time.Instant;

public record OperationDTO(
        Long id,
        OperationType type,
        BigDecimal amount,
        BankAccountDTO homeAccount,
        BankAccountDTO targetAccount,
        Instant date) {}
