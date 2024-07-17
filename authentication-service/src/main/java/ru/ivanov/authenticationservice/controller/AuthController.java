package ru.ivanov.authenticationservice.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.ivanov.authenticationservice.dto.request.AccessTokenRequestDto;
import ru.ivanov.authenticationservice.dto.request.JwtRequestDto;
import ru.ivanov.authenticationservice.dto.request.RefreshJwtRequestDto;
import ru.ivanov.authenticationservice.dto.response.JwtResponseDto;
import ru.ivanov.authenticationservice.dto.response.ValidateAccessTokenResponseDto;

@RequestMapping(value = "/api/v1/auth")
public interface AuthController {

    @PostMapping(value = "/login")
    ResponseEntity<JwtResponseDto> login(@RequestBody JwtRequestDto authRequest);

    @PostMapping(value = "/refresh-token")
    ResponseEntity<JwtResponseDto> refresh(@RequestBody RefreshJwtRequestDto refreshJwtRequestDto);

    @PostMapping(value = "/validation")
    ResponseEntity<ValidateAccessTokenResponseDto> validateAccessToken(@RequestBody AccessTokenRequestDto accessTokenRequest);

}
