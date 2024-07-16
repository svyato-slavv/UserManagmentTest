package ru.ivanov.userservice.exception;

public class EmailAlreadyTakenException extends RuntimeException{

    public EmailAlreadyTakenException(String message) {
        super(message);
    }

}
