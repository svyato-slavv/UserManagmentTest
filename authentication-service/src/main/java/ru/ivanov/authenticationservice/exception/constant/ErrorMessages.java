package ru.ivanov.authenticationservice.exception.constant;

import lombok.Getter;

@Getter
public enum ErrorMessages {

    USER_CREDENTIAL_NOT_FOUND("Неверный логин или пароль!"),

    USER_NOT_FOUND("Неверный логин"),

    ACCOUNT_IS_BLOCKED("Пользователь заблокирован"),

    REFRESH_TOKEN_NOT_FOUND("refresh token не найден в БД"),

    REFRESH_TOKEN_HAS_EXPIRED("Истек срок действия refresh-токена"),

    INVALID_REFRESH_TOKEN_SIGNATURE("Подпись refresh-токена недействительна"),

    SERVER_ERROR("Непредвиденная ошибка. Попробуйте позже!"),

    ACCESS_TOKEN_HAS_EXPIRED("Срок жизни access-токена истек"),

    ACCESS_TOKEN_SIGNATURE_IS_INVALID("Подпись access-токена недействительна"),

    INTERNAL_SERVER_ERROR("Непредвиденная ошибка. Попробуйте позже"),

    TOKEN_NOT_VALID("Невалидный токен");

    private final String message;

    ErrorMessages(String message) {
        this.message = message;
    }

}
