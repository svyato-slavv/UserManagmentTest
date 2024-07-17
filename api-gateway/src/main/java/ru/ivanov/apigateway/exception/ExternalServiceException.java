package ru.ivanov.apigateway.exception;

import lombok.Getter;

@Getter
public class ExternalServiceException extends RuntimeException {

    int code;

    public ExternalServiceException(String message, int code) {
        super(message);
        this.code = code;
    }

}
