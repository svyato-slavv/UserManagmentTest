package ru.ivanov.authenticationservice.exception;

public class RefreshTokenExpiredException extends RuntimeException{

    public RefreshTokenExpiredException(String message) {
        super(message);
    }

}
