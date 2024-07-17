package ru.ivanov.apigateway.exception;

public class AccessTokenNotFoundException extends RuntimeException {

    public AccessTokenNotFoundException(String message) {
        super(message);
    }

}
