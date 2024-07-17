package ru.ivanov.authenticationservice.exception;

public class RefreshTokenNotFoundException extends RuntimeException {

    public RefreshTokenNotFoundException(String message) {
        super(message);
    }

}
