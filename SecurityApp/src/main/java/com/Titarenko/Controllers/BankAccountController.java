package com.Titarenko.Controllers;

import com.Titarenko.DTO.BankAccountDTO;
import com.Titarenko.DTO.OperationDTO;
import com.Titarenko.Services.BankAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.List;
@RestController
@RequestMapping("/bankAccounts")
public class BankAccountController {
    @Value("${bank.service.url}")
    private String bankServiceUrl;
    private final RestTemplate restTemplate = new RestTemplate();
    private final BankAccountService bankAccountService;

    @Autowired
    public BankAccountController(BankAccountService bankAccountService) {
        this.bankAccountService = bankAccountService;
    }

    @GetMapping("/admin/all")
    public ResponseEntity<List<BankAccountDTO>> getAllAccounts() {
        return restTemplate.exchange(
                bankServiceUrl + "/bankAccounts/all",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {}
        );
    }

    @GetMapping("/admin/user/{login}")
    public ResponseEntity<List<BankAccountDTO>> getAccountsByUser(@PathVariable("login") String login) {
        return restTemplate.exchange(
                bankServiceUrl + "/bankAccounts/user/" + login,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {}
        );
    }

    @GetMapping("/admin/{id}/operations")
    public ResponseEntity<List<OperationDTO>> getAccountOperations(@PathVariable("id") Long id) {
        return restTemplate.exchange(
                bankServiceUrl + "/bankAccounts/" + id + "/operations",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {}
        );
    }

    @PostMapping("/client/create")
    public ResponseEntity<BankAccountDTO> createAccount(Authentication authentication) {
        return restTemplate.exchange(
                bankServiceUrl + "/bankAccounts/create/" + authentication.getName(),
                HttpMethod.POST,
                null,
                BankAccountDTO.class
        );
    }

    @GetMapping("/client/all")
    public ResponseEntity<List<BankAccountDTO>> getCurrentUserAccounts(Authentication authentication) {
        String login = authentication.getName();
        return restTemplate.exchange(
                bankServiceUrl + "/bankAccounts/user/" + login,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {}
        );
    }

    @GetMapping("/client/{id}")
    public ResponseEntity<BankAccountDTO> getAccount(@PathVariable("id") Long id,
                                        Authentication authentication) {
        String login = authentication.getName();
        ResponseEntity<List<BankAccountDTO>> response = restTemplate.exchange(
                bankServiceUrl + "/bankAccounts/all",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {}
        );

        return new ResponseEntity<>(bankAccountService.getAvailableBankAccount(
                response.getBody(),
                id,
                login
        ), HttpStatus.OK);
    }

    @PostMapping("/client/{id}/deposit/{amount}")
    public ResponseEntity<String> deposit(@PathVariable("id") Long id,
                                          @PathVariable("amount") BigDecimal amount,
                                          Authentication authentication) {
        String login = authentication.getName();
        ResponseEntity<List<BankAccountDTO>> response = restTemplate.exchange(
                bankServiceUrl + "/bankAccounts/all",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {}
        );
        bankAccountService.getAvailableBankAccount(
                response.getBody(),
                id,
                login
        );

        return restTemplate.exchange(
                bankServiceUrl + "/bankAccounts/" + id + "/deposit/" + amount,
                HttpMethod.POST,
                null,
                String.class
        );
    }

    @PostMapping("/client/{id}/withdraw/{amount}")
    public ResponseEntity<String> withdraw(@PathVariable("id") Long id,
                                           @PathVariable("amount") BigDecimal amount,
                                           Authentication authentication) {
        String login = authentication.getName();
        ResponseEntity<List<BankAccountDTO>> response = restTemplate.exchange(
                bankServiceUrl + "/bankAccounts/all",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {}
        );
        bankAccountService.getAvailableBankAccount(
                response.getBody(),
                id,
                login
        );

        return restTemplate.exchange(
                bankServiceUrl + "/bankAccounts/" + id + "/withdraw/" + amount,
                HttpMethod.POST,
                null,
                String.class
        );
    }

    @PostMapping("/client/{homeAccountId}/transfer/{targetAccountId}/{amount}")
    public ResponseEntity<String> transfer(@PathVariable("homeAccountId") Long homeAccountId,
                                           @PathVariable("targetAccountId") Long targetAccountId,
                                           @PathVariable("amount") BigDecimal amount,
                                           Authentication authentication) {
        String login = authentication.getName();
        ResponseEntity<List<BankAccountDTO>> response = restTemplate.exchange(
                bankServiceUrl + "/bankAccounts/all",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {}
        );
        bankAccountService.getAvailableBankAccount(
                response.getBody(),
                homeAccountId,
                login
        );

        return restTemplate.exchange(
                bankServiceUrl + "/bankAccounts/" + homeAccountId + "/transfer/" + targetAccountId + "/" + amount,
                HttpMethod.POST,
                null,
                String.class
        );
    }
}
