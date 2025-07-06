package com.Titarenko.Controllers;

import Exceptions.AccountNotFoundException;
import Exceptions.NotEnoughBalanceException;
import Exceptions.UserNotFoundException;
import Models.BankAccount;
import Models.OperationType;
import Services.BankAccountService;
import com.Titarenko.DTO.BankAccountDTO;
import com.Titarenko.DTO.OperationDTO;
import com.Titarenko.Mappers.BankAccountMapper;
import com.Titarenko.Mappers.OperationMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/bankAccounts")
public class BankAccountController {
    private final BankAccountService bankAccountService;

    @Autowired
    public BankAccountController(BankAccountService bankAccountService) {
        this.bankAccountService = bankAccountService;
    }

    @Operation(summary = "Create new bank account")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Account created successfully"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @PostMapping("/create/{ownerLogin}")
    public ResponseEntity<BankAccountDTO> createAccount(@PathVariable("ownerLogin") String ownerLogin)
            throws UserNotFoundException {
        BankAccount account = bankAccountService.CreateAccount(ownerLogin);

        return new ResponseEntity<>(BankAccountMapper.toDTO(account), HttpStatus.CREATED);
    }

    @Operation(summary = "Get account balance")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Balance provided"),
            @ApiResponse(responseCode = "404", description = "Account not found")
    })
    @GetMapping("/{id}/balance")
    public ResponseEntity<BigDecimal> getBalance(@PathVariable("id") Long id) throws AccountNotFoundException {
        BigDecimal balance = bankAccountService.GetBalance(id);

        return ResponseEntity.ok(balance);
    }

    @Operation(summary = "Withdraw money from account")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Withdrawal successful"),
            @ApiResponse(responseCode = "400", description = "Not enough balance"),
            @ApiResponse(responseCode = "404", description = "Account not found")
    })
    @PostMapping("/{id}/withdraw/{amount}")
    public ResponseEntity<String> withdraw(@PathVariable("id") Long id,
                                           @PathVariable("amount") BigDecimal amount)
            throws NotEnoughBalanceException, AccountNotFoundException {
        bankAccountService.Withdraw(id, amount);

        return ResponseEntity.ok("Withdrawal successful");
    }

    @Operation(summary = "Deposit money to account")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Deposit successful"),
            @ApiResponse(responseCode = "404", description = "Account not found")
    })
    @PostMapping("/{id}/deposit/{amount}")
    public ResponseEntity<String> deposit(@PathVariable("id") Long id,
                                          @PathVariable("amount") BigDecimal amount) throws AccountNotFoundException {
        bankAccountService.Deposit(id, amount);

        return ResponseEntity.ok("Deposit successful");
    }

    @Operation(summary = "Transfer money from one account to another")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Transfer successful"),
            @ApiResponse(responseCode = "400", description = "Not enough balance"),
            @ApiResponse(responseCode = "404", description = "Account not found")
    })
    @PostMapping("/{homeAccountId}/transfer/{targetAccountId}/{amount}")
    public ResponseEntity<String> transfer(@PathVariable("homeAccountId") Long homeAccountId,
                                           @PathVariable("targetAccountId") Long targetAccountId,
                                           @PathVariable("amount") BigDecimal amount)
            throws NotEnoughBalanceException, AccountNotFoundException {
        bankAccountService.Transfer(homeAccountId, targetAccountId, amount);

        return ResponseEntity.ok("Transfer successful");
    }

    @Operation(summary = "Get all bank accounts of user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Accounts provided"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @GetMapping("/user/{login}")
    public ResponseEntity<List<BankAccountDTO>> getAccountsByUser(@PathVariable("login") String login)
            throws UserNotFoundException {
        List<BankAccount> accounts = bankAccountService.getBankAccountsByOwnerLogin(login);
        List<BankAccountDTO> accountsDTO = new ArrayList<>();
        for (BankAccount account : accounts) {
            accountsDTO.add(BankAccountMapper.toDTO(account));
        }

        return ResponseEntity.ok(accountsDTO);
    }

    @Operation(summary = "Get all bank accounts")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Accounts provided")
    })
    @GetMapping("/all")
    public ResponseEntity<List<BankAccountDTO>> getAllAccounts() {
        List<BankAccount> accounts = bankAccountService.getAllBankAccounts();
        List<BankAccountDTO> accountsDTO = new ArrayList<>();
        for (BankAccount account : accounts) {
            accountsDTO.add(BankAccountMapper.toDTO(account));
        }

        return ResponseEntity.ok(accountsDTO);
    }

    @Operation(summary = "Get operations by type")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operations provided")
    })
    @GetMapping("/operations/{type}")
    public ResponseEntity<List<OperationDTO>> getOperationsByType(@PathVariable("type") OperationType type) {
        List<Models.Operation> operations = bankAccountService.getOperationsByType(type);
        List<OperationDTO> operationsDTO = new ArrayList<>();
        for (Models.Operation operation : operations) {
            operationsDTO.add(OperationMapper.toDTO(operation));
        }

        return ResponseEntity.ok(operationsDTO);
    }

    @Operation(summary = "Get all operations for account")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operations provided"),
            @ApiResponse(responseCode = "404", description = "Account not found")
    })
    @GetMapping("/{id}/operations")
    public ResponseEntity<List<OperationDTO>> getAccountOperations(@PathVariable("id") Long id)
            throws AccountNotFoundException {
        List<Models.Operation> operations = bankAccountService.getOperationsByHomeAccountId(id);
        List<OperationDTO> operationsDTO = new ArrayList<>();
        for (Models.Operation operation : operations) {
            operationsDTO.add(OperationMapper.toDTO(operation));
        }
        return ResponseEntity.ok(operationsDTO);
    }
}
