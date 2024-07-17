package ru.ivanov.authenticationservice.service;

import org.springframework.security.core.userdetails.UserDetailsService;
import ru.ivanov.authenticationservice.data.model.AdminCredential;
import ru.ivanov.authenticationservice.dto.request.JwtRequestDto;
import ru.ivanov.authenticationservice.dto.response.JwtResponseDto;

public interface UserService extends UserDetailsService {

    JwtResponseDto generateNewPairForLogin(JwtRequestDto jwtRequestDto);

    JwtResponseDto refreshTokens(String refreshToken);

    AdminCredential getByLogin(String login);

    void checkLoginAndPassword(JwtRequestDto authRequest);

}
