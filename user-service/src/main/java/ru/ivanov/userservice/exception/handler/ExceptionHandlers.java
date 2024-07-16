package ru.ivanov.userservice.exception.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;
import ru.ivanov.userservice.exception.EmailAlreadyTakenException;
import ru.ivanov.userservice.exception.NotFoundException;
import ru.ivanov.userservice.exception.constant.ErrorMessages;
import ru.ivanov.userservice.exception.response.ErrorResponseDto;

@RestControllerAdvice
public class ExceptionHandlers {

    @ExceptionHandler(NotFoundException.class)
    private ResponseEntity<ErrorResponseDto> handler(NotFoundException e) {
        ErrorResponseDto errorResponseDto = new ErrorResponseDto(e.getMessage());
        return new ResponseEntity<>(errorResponseDto, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(EmailAlreadyTakenException.class)
    private ResponseEntity<ErrorResponseDto> handler(EmailAlreadyTakenException e) {
        ErrorResponseDto errorResponseDto = new ErrorResponseDto(e.getMessage());
        return new ResponseEntity<>(errorResponseDto, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    protected ResponseEntity<Object> handleNoHandlerFoundException(NoHandlerFoundException e) {
        ErrorResponseDto errorResponseDto = new ErrorResponseDto(ErrorMessages.NO_HANDLER_FOUND.getMessage());
        return new ResponseEntity<>(errorResponseDto, HttpStatus.NOT_FOUND);
    }

}
