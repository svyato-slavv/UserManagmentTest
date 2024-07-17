package ru.ivanov.authenticationservice.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.ivanov.authenticationservice.data.model.RefreshToken;
import ru.ivanov.authenticationservice.data.repository.RefreshTokenRepository;
import ru.ivanov.authenticationservice.service.RefreshTokenService;

@Service
@RequiredArgsConstructor
public class RefreshTokenServiceImpl implements RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;

    @Override
    public RefreshToken getRefreshTokenFromDb(String login) {
        return refreshTokenRepository.findByAdminLogin(login)
                .orElse(null);
    }

    @Override
    public void saveRefreshToken(RefreshToken refreshToken) {
        refreshTokenRepository.save(refreshToken);
    }

    @Override
    public void updateRefreshToken(RefreshToken refreshTokenFromDb, RefreshToken refreshTokenNew) {
        refreshTokenFromDb.setRefreshToken(refreshTokenNew.getRefreshToken());
        refreshTokenFromDb.setExpire(refreshTokenNew.getExpire());
        refreshTokenRepository.save(refreshTokenFromDb);
    }

    @Override
    public void deleteRefreshToken(String refreshToken) {
        refreshTokenRepository.deleteByRefreshToken(refreshToken);
    }

}
