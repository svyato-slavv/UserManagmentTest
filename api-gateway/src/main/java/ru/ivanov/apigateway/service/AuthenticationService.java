package ru.ivanov.apigateway.service;

import org.springframework.http.ResponseEntity;
import ru.ivanov.apigateway.dto.response.ValidateAccessTokenResponseDto;

public interface AuthenticationService {

    ResponseEntity<ValidateAccessTokenResponseDto> validate(String accessToken);

}
