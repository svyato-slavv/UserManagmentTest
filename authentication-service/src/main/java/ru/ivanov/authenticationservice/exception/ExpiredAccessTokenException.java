package ru.ivanov.authenticationservice.exception;

public class ExpiredAccessTokenException extends RuntimeException{

    public ExpiredAccessTokenException(String message){
        super(message);
    }
}
