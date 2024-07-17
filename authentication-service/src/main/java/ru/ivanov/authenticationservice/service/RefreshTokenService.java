package ru.ivanov.authenticationservice.service;

import ru.ivanov.authenticationservice.data.model.RefreshToken;

public interface RefreshTokenService {

    RefreshToken getRefreshTokenFromDb(String login);

    void saveRefreshToken(RefreshToken refreshToken);

    void updateRefreshToken(RefreshToken refreshTokenFromDb, RefreshToken refreshTokenNew);

    void deleteRefreshToken(String refreshToken);

}
