package ru.ivanov.authenticationservice.exception.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.ivanov.authenticationservice.exception.AccessTokenSignatureIsNotValidException;
import ru.ivanov.authenticationservice.exception.AccountBlockedException;
import ru.ivanov.authenticationservice.exception.ExpiredAccessTokenException;
import ru.ivanov.authenticationservice.exception.IncorrectPasswordException;
import ru.ivanov.authenticationservice.exception.RefreshSignatureNotValidException;
import ru.ivanov.authenticationservice.exception.RefreshTokenExpiredException;
import ru.ivanov.authenticationservice.exception.RefreshTokenNotFoundException;
import ru.ivanov.authenticationservice.exception.TokenNotValidException;
import ru.ivanov.authenticationservice.exception.UserNotFoundException;
import ru.ivanov.authenticationservice.exception.dto.ErrorResponseDto;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UserNotFoundException.class)
    ResponseEntity<ErrorResponseDto> exceptionHandler(UserNotFoundException e) {
        return new ResponseEntity<>(new ErrorResponseDto(e.getMessage()), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(IncorrectPasswordException.class)
    ResponseEntity<ErrorResponseDto> exceptionHandler(IncorrectPasswordException e) {
        return new ResponseEntity<>(new ErrorResponseDto(e.getMessage()), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(RefreshTokenNotFoundException.class)
    ResponseEntity<ErrorResponseDto> exceptionHandler(RefreshTokenNotFoundException e) {
        return new ResponseEntity<>(new ErrorResponseDto(e.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(RefreshTokenExpiredException.class)
    ResponseEntity<ErrorResponseDto> exceptionHandler(RefreshTokenExpiredException e) {
        return new ResponseEntity<>(new ErrorResponseDto(e.getMessage()), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(RefreshSignatureNotValidException.class)
    ResponseEntity<ErrorResponseDto> exceptionHandler(RefreshSignatureNotValidException e) {
        return new ResponseEntity<>(new ErrorResponseDto(e.getMessage()), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(ExpiredAccessTokenException.class)
    ResponseEntity<ErrorResponseDto> exceptionHandler(ExpiredAccessTokenException e) {
        return new ResponseEntity<>(new ErrorResponseDto(e.getMessage()), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(AccessTokenSignatureIsNotValidException.class)
    ResponseEntity<ErrorResponseDto> exceptionHandler(AccessTokenSignatureIsNotValidException e) {
        return new ResponseEntity<>(new ErrorResponseDto(e.getMessage()), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(TokenNotValidException.class)
    ResponseEntity<ErrorResponseDto> exceptionHandler(TokenNotValidException e) {
        return new ResponseEntity<>(new ErrorResponseDto(e.getMessage()), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(AccountBlockedException.class)
    ResponseEntity<ErrorResponseDto> exceptionHandler(AccountBlockedException e) {
        return new ResponseEntity<>(new ErrorResponseDto(e.getMessage()), HttpStatus.NOT_ACCEPTABLE);
    }

}
