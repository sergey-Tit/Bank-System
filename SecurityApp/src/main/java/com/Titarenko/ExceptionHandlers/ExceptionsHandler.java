package com.Titarenko.ExceptionHandlers;

import com.Titarenko.Exceptions.AccountNotFoundException;
import com.Titarenko.Exceptions.ClientNotOwnerException;
import com.Titarenko.Exceptions.LoginAlreadyExistException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpClientErrorException;

@ControllerAdvice
public class ExceptionsHandler {
    @ExceptionHandler(LoginAlreadyExistException.class)
    public ResponseEntity<String> handleLoginAlreadyExistException(LoginAlreadyExistException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(ClientNotOwnerException.class)
    public ResponseEntity<String> handleClientNotOwnerException(ClientNotOwnerException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(HttpClientErrorException.class)
    public ResponseEntity<String> handleHttpClientErrorException(HttpClientErrorException ex) {
        return new ResponseEntity<>(ex.getMessage(), ex.getStatusCode());
    }

    @ExceptionHandler(AccountNotFoundException.class)
    public ResponseEntity<String> handleAccountNotFoundException(AccountNotFoundException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }
}
