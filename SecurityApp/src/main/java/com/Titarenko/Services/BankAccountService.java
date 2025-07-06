package com.Titarenko.Services;

import com.Titarenko.DTO.BankAccountDTO;
import com.Titarenko.Exceptions.AccountNotFoundException;
import com.Titarenko.Exceptions.ClientNotOwnerException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BankAccountService {
    public BankAccountService() {}

    public BankAccountDTO getAvailableBankAccount(List<BankAccountDTO> accounts, Long id, String login)
            throws AccountNotFoundException, ClientNotOwnerException {
        for (BankAccountDTO account : accounts) {
            if (account.id().equals(id)) {
                if (account.owner().login().equals(login)) {
                    return account;
                }
                else {
                    throw new ClientNotOwnerException("Client is not owner of this account.");
                }
            }
        }

        throw new AccountNotFoundException("Account not found.");
    }
}
