package ru.ivanov.authenticationservice.exception;

public class AccountBlockedException extends RuntimeException{

    public AccountBlockedException(String message) {
        super(message);
    }

}
