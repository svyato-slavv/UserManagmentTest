package ru.ivanov.authenticationservice.exception.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class ErrorResponseDto {

    private String errorMessage;

}
