package ru.ivanov.userservice.exception.constant;

import lombok.Getter;

@Getter
public enum ErrorMessages {

    EMAIL_ALREADY_TAKEN("Email already taken by another one!"),

    NO_HANDLER_FOUND("No handler found!");

    private final String message;

    ErrorMessages(String message) {
        this.message = message;
    }

}
