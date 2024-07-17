package ru.ivanov.authenticationservice.exception;

public class AccessTokenSignatureIsNotValidException extends RuntimeException{

    public AccessTokenSignatureIsNotValidException(String message) {
        super(message);
    }
}
