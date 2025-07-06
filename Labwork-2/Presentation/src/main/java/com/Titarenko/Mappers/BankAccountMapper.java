package com.Titarenko.Mappers;

import com.Titarenko.DTO.BankAccountDTO;
import Models.BankAccount;

public class BankAccountMapper {
    public static BankAccountDTO toDTO(BankAccount bankAccount) {
        if (bankAccount == null) {
            return null;
        }

        return new BankAccountDTO(
                bankAccount.getId(),
                bankAccount.getBalance(),
                UserMapper.toDTO(bankAccount.getOwner())
        );
    }
}
