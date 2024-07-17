package ru.ivanov.authenticationservice.exception;

public class TokenNotValidException extends RuntimeException {

    public TokenNotValidException(String message) {
        super(message);
    }

}
