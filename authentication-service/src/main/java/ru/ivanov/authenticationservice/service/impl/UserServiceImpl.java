package ru.ivanov.authenticationservice.service.impl;

import io.jsonwebtoken.Claims;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.ivanov.authenticationservice.data.model.AdminCredential;
import ru.ivanov.authenticationservice.data.model.RefreshToken;
import ru.ivanov.authenticationservice.data.repository.AdminCredentialRepository;
import ru.ivanov.authenticationservice.dto.request.JwtRequestDto;
import ru.ivanov.authenticationservice.dto.response.JwtResponseDto;
import ru.ivanov.authenticationservice.exception.AccountBlockedException;
import ru.ivanov.authenticationservice.exception.IncorrectPasswordException;
import ru.ivanov.authenticationservice.exception.RefreshTokenNotFoundException;
import ru.ivanov.authenticationservice.exception.UserNotFoundException;
import ru.ivanov.authenticationservice.exception.constant.ErrorMessages;
import ru.ivanov.authenticationservice.service.RefreshTokenService;
import ru.ivanov.authenticationservice.service.UserService;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final AdminCredentialRepository userRepository;

    private final RefreshTokenService refreshTokenService;

    private final JwtProviderServiceImpl jwtProviderService;

    private final PasswordEncoder passwordEncoder;

    private final Integer ATTEMPTS_COUNT = 3;

    private final Map<String, Integer> verificationPasswordMap = new ConcurrentHashMap<>();

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByLogin(username)
                .orElseThrow(() -> new UserNotFoundException(ErrorMessages.USER_CREDENTIAL_NOT_FOUND.getMessage()));
    }

    @Transactional
    public JwtResponseDto generateNewPairForLogin(JwtRequestDto jwtRequestDto) {
        final AdminCredential user = getByLogin(jwtRequestDto.getLogin());
        String accessTokenNew = jwtProviderService.generateAccessToken(user);
        RefreshToken refreshTokenNew = jwtProviderService.generateRefreshToken(user);
        RefreshToken refreshTokenFromDb = refreshTokenService.getRefreshTokenFromDb(user.getLogin());
        if (refreshTokenFromDb == null) {
            refreshTokenService.saveRefreshToken(refreshTokenNew);
        } else {
            refreshTokenService.updateRefreshToken(refreshTokenFromDb, refreshTokenNew);
        }
        return JwtResponseDto.builder()
                .accessToken(accessTokenNew)
                .refreshToken(refreshTokenNew.getRefreshToken())
                .build();
    }

    @Override
    public JwtResponseDto refreshTokens(String refreshToken) {
        jwtProviderService.validateRefreshToken(refreshToken);
        final Claims claims = jwtProviderService.getRefreshClaims(refreshToken);
        final String login = claims.getSubject();
        RefreshToken refreshTokenFromDb = refreshTokenService.getRefreshTokenFromDb(login);
        if (refreshTokenFromDb == null) {
            throw new RefreshTokenNotFoundException(ErrorMessages.REFRESH_TOKEN_NOT_FOUND.getMessage());
        }
        if (refreshTokenFromDb.getRefreshToken().equals(refreshToken)) {
            AdminCredential user = refreshTokenFromDb.getAdminCredential();
            String accessTokenNew = jwtProviderService.generateAccessToken(user);
            RefreshToken refreshTokenNew = jwtProviderService.generateRefreshToken(user);
            refreshTokenService.updateRefreshToken(refreshTokenFromDb, refreshTokenNew);
            return new JwtResponseDto(accessTokenNew, refreshTokenNew.getRefreshToken());
        } else
            throw new RefreshTokenNotFoundException(ErrorMessages.REFRESH_TOKEN_NOT_FOUND.getMessage());
    }

    public AdminCredential getByLogin(@NonNull String login) {
        return userRepository.findByLogin(login)
                .orElseThrow(() -> new UserNotFoundException(ErrorMessages.USER_NOT_FOUND.getMessage()));
    }

    private Integer getAttempts(String login) {
        verificationPasswordMap.putIfAbsent(login, ATTEMPTS_COUNT);
        return verificationPasswordMap.merge(login, 1, (prev, one) -> prev - one);
    }

    @Override
    public void checkLoginAndPassword(JwtRequestDto authRequest) {
        AdminCredential admin = getByLogin(authRequest.getLogin());
        if (admin.getIsBlocked()) {
            throw new AccountBlockedException(ErrorMessages.ACCOUNT_IS_BLOCKED.getMessage());
        }
        if (!passwordEncoder.matches(authRequest.getPassword(), admin.getPassword())) {
            Integer attempts = getAttempts(admin.getLogin());
            if (attempts == 0) {
                blockAccount(admin);
                throw new AccountBlockedException(ErrorMessages.ACCOUNT_IS_BLOCKED.getMessage());
            }
            if (attempts == 1) {
                throw new IncorrectPasswordException("Неверный пароль. Осталась " + attempts + " попытка");
            }
            throw new IncorrectPasswordException("Неверный пароль. Осталось " + attempts + " попытки");
        }
        verificationPasswordMap.remove(admin.getLogin());
    }

    private void blockAccount(AdminCredential admin) {
        admin.setIsBlocked(true);
        userRepository.save(admin);
        verificationPasswordMap.remove(admin.getLogin());
    }

}