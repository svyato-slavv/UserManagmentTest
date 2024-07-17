package ru.ivanov.authenticationservice.controller.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.ivanov.authenticationservice.controller.AuthController;
import ru.ivanov.authenticationservice.dto.request.AccessTokenRequestDto;
import ru.ivanov.authenticationservice.dto.request.JwtRequestDto;
import ru.ivanov.authenticationservice.dto.request.RefreshJwtRequestDto;
import ru.ivanov.authenticationservice.dto.response.JwtResponseDto;
import ru.ivanov.authenticationservice.dto.response.ValidateAccessTokenResponseDto;
import ru.ivanov.authenticationservice.service.JwtProviderService;
import ru.ivanov.authenticationservice.service.UserService;

@RestController
@RequiredArgsConstructor
public class AuthControllerImpl implements AuthController {

    private final UserService userService;

    private final JwtProviderService jwtProviderService;

    private final AuthenticationManager authenticationManager;

    @Override
    public ResponseEntity<JwtResponseDto> login(@RequestBody JwtRequestDto authRequest) {
        userService.checkLoginAndPassword(authRequest);
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authRequest.getLogin(), authRequest.getPassword())
        );
        return new ResponseEntity<>(userService.generateNewPairForLogin(authRequest), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<JwtResponseDto> refresh(RefreshJwtRequestDto refreshJwtRequestDto) {
        JwtResponseDto newPair = userService.refreshTokens(refreshJwtRequestDto.getRefreshToken());
        return new ResponseEntity<>(newPair, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<ValidateAccessTokenResponseDto> validateAccessToken(@RequestBody AccessTokenRequestDto accessTokenRequest) {
        ValidateAccessTokenResponseDto validateAccessTokenResponseDto = jwtProviderService.validateAccessToken(accessTokenRequest.getAccessToken());
        return new ResponseEntity<>(validateAccessTokenResponseDto, HttpStatus.OK);
    }

}
