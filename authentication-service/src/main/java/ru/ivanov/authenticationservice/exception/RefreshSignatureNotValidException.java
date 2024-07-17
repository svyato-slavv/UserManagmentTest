package ru.ivanov.authenticationservice.exception;

public class RefreshSignatureNotValidException extends RuntimeException {

    public RefreshSignatureNotValidException(String message) {
        super(message);
    }

}
