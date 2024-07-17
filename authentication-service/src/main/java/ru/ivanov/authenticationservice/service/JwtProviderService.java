package ru.ivanov.authenticationservice.service;

import io.jsonwebtoken.Claims;
import lombok.NonNull;
import ru.ivanov.authenticationservice.data.model.AdminCredential;
import ru.ivanov.authenticationservice.data.model.RefreshToken;
import ru.ivanov.authenticationservice.dto.response.ValidateAccessTokenResponseDto;

public interface JwtProviderService {

    String generateAccessToken(@NonNull AdminCredential user);

    RefreshToken generateRefreshToken(@NonNull AdminCredential user);

    ValidateAccessTokenResponseDto validateAccessToken(@NonNull String accessToken);

    void validateRefreshToken(@NonNull String refreshToken);

    Claims getRefreshClaims(@NonNull String token);

}
