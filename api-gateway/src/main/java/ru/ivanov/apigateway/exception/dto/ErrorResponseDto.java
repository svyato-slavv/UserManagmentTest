package ru.ivanov.apigateway.exception.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class ErrorResponseDto {

    private String errorMessage;

}
