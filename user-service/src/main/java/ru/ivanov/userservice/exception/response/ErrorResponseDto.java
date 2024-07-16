package ru.ivanov.userservice.exception.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class ErrorResponseDto {

    private String errorMessage;

}
