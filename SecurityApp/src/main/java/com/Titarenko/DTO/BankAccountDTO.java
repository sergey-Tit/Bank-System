package com.Titarenko.DTO;

import java.math.BigDecimal;

public record BankAccountDTO(Long id, BigDecimal balance, UserDTO owner) {}
