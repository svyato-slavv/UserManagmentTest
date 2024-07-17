package ru.ivanov.apigateway.exception.constant;

import lombok.Getter;

@Getter
public enum ErrorMessages {

    ACCESS_TOKEN_HAS_EXPIRED("Срок жизни access-токена истек"),

    ACCESS_TOKEN_NOT_FOUND("Неверный логин или пароль"),

    ACCESS_TOKEN_SIGNATURE_IS_INVALID("Подпись access-токена недействительна"),

    INTERNAL_SERVER_ERROR("Непредвиденная ошибка. Попробуйте позже");

    private final String message;

    ErrorMessages(String message) {
        this.message = message;
    }

}
