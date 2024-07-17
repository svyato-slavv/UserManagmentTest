package ru.ivanov.authenticationservice.service.impl;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.ivanov.authenticationservice.data.model.AdminCredential;
import ru.ivanov.authenticationservice.data.model.RefreshToken;
import ru.ivanov.authenticationservice.dto.response.ValidateAccessTokenResponseDto;
import ru.ivanov.authenticationservice.exception.AccessTokenSignatureIsNotValidException;
import ru.ivanov.authenticationservice.exception.ExpiredAccessTokenException;
import ru.ivanov.authenticationservice.exception.RefreshSignatureNotValidException;
import ru.ivanov.authenticationservice.exception.RefreshTokenExpiredException;
import ru.ivanov.authenticationservice.exception.TokenNotValidException;
import ru.ivanov.authenticationservice.exception.constant.ErrorMessages;
import ru.ivanov.authenticationservice.service.JwtProviderService;
import ru.ivanov.authenticationservice.service.RefreshTokenService;

import javax.crypto.SecretKey;
import java.security.Key;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Slf4j
@Service
public class JwtProviderServiceImpl implements JwtProviderService {

    private final SecretKey jwtAccessSecret;

    private final SecretKey jwtRefreshSecret;

    private final String jwtAccessInMinutesLifeTime;

    private final String jwtRefreshInDaysLifeTime;

    private final RefreshTokenService refreshTokenService;


    public JwtProviderServiceImpl(
            @Value("${jwt.secret.access}") String jwtAccessSecret,
            @Value("${jwt.secret.accessInMinutesLifeTime}") String jwtAccessInMinutesLifeTime,
            @Value("${jwt.secret.refresh}") String jwtRefreshSecret,
            @Value("${jwt.secret.refreshInDaysLifeTime}") String jwtRefreshInDaysLifeTime,
            RefreshTokenService refreshTokenService
    ) {
        this.jwtAccessSecret = Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtAccessSecret));
        this.jwtAccessInMinutesLifeTime = jwtAccessInMinutesLifeTime;
        this.jwtRefreshSecret = Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtRefreshSecret));
        this.jwtRefreshInDaysLifeTime = jwtRefreshInDaysLifeTime;
        this.refreshTokenService = refreshTokenService;
    }

    public String generateAccessToken(@NonNull AdminCredential user) {
        final LocalDateTime now = LocalDateTime.now();
        final Instant accessExpirationInstant = now.plusMinutes(Long.parseLong(jwtAccessInMinutesLifeTime))
                .atZone(ZoneId.systemDefault()).toInstant();
        final Date accessExpiration = Date.from(accessExpirationInstant);
        return Jwts.builder()
                .setSubject(user.getLogin())
                .setExpiration(accessExpiration)
                .signWith(jwtAccessSecret)
                .claim("admin_id", user.getId())
                .compact();
    }

    public RefreshToken generateRefreshToken(@NonNull AdminCredential user) {
        final LocalDateTime now = LocalDateTime.now();
        final Instant refreshExpirationInstant = now.plusDays(Long.parseLong(jwtRefreshInDaysLifeTime))
                .atZone(ZoneId.systemDefault()).toInstant();
        final Date refreshExpiration = Date.from(refreshExpirationInstant);
        String token = Jwts.builder()
                .setSubject(user.getLogin())
                .setExpiration(refreshExpiration)
                .signWith(jwtRefreshSecret)
                .compact();
        LocalDateTime expireDateTime = refreshExpirationInstant.atZone(ZoneId.systemDefault()).toLocalDateTime();
        return RefreshToken.builder()
                .refreshToken(token)
                .expire(expireDateTime)
                .adminCredential(user)
                .build();
    }

    public ValidateAccessTokenResponseDto validateAccessToken(@NonNull String accessToken) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(jwtAccessSecret)
                    .build()
                    .parseClaimsJws(accessToken);
        } catch (ExpiredJwtException expEx) {
            log.error("Token expired", expEx);
            throw new ExpiredAccessTokenException(ErrorMessages.ACCESS_TOKEN_HAS_EXPIRED.getMessage());
        } catch (SignatureException sEx) {
            log.error("Invalid signature", sEx);
            throw new AccessTokenSignatureIsNotValidException(ErrorMessages.ACCESS_TOKEN_SIGNATURE_IS_INVALID.getMessage());
        } catch (MalformedJwtException mEx) {
            log.error("Invalid token", mEx);
            throw new TokenNotValidException(ErrorMessages.ACCESS_TOKEN_SIGNATURE_IS_INVALID.getMessage());
        } catch (Exception e) {
            log.error("Server error", e);
            throw new RuntimeException(ErrorMessages.TOKEN_NOT_VALID.getMessage());
        }
        return getAdminCredential(accessToken);
    }

    public void validateRefreshToken(@NonNull String refreshToken) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(jwtRefreshSecret)
                    .build()
                    .parseClaimsJws(refreshToken);
        } catch (ExpiredJwtException expEx) {
            log.error("Token expired", expEx);
            refreshTokenService.deleteRefreshToken(refreshToken);
            throw new RefreshTokenExpiredException(ErrorMessages.REFRESH_TOKEN_HAS_EXPIRED.getMessage());
        } catch (SignatureException sEx) {
            log.error("Invalid signature", sEx);
            throw new RefreshSignatureNotValidException(ErrorMessages.INVALID_REFRESH_TOKEN_SIGNATURE.getMessage());
        } catch (Exception e) {
            log.error("invalid token", e);
            throw new RuntimeException(ErrorMessages.INTERNAL_SERVER_ERROR.getMessage());
        }
    }

    private ValidateAccessTokenResponseDto getAdminCredential(@NonNull String accessToken) {
        return ValidateAccessTokenResponseDto.builder()
                .adminCredentialId(getClaims(accessToken, jwtAccessSecret).get("admin_id", Integer.class))
                .build();
    }

    public Claims getRefreshClaims(@NonNull String token) {
        return getClaims(token, jwtRefreshSecret);
    }

    private Claims getClaims(@NonNull String token, @NonNull Key secret) {
        return Jwts.parserBuilder()
                .setSigningKey(secret)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

}
