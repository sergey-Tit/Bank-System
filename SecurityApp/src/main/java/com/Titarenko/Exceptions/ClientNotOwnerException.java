package com.Titarenko.Exceptions;

public class ClientNotOwnerException extends RuntimeException {
    public ClientNotOwnerException(String message) {
        super(message);
    }
}
