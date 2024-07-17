package ru.ivanov.apigateway.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.ivanov.apigateway.dto.request.AccessTokenRequestDto;
import ru.ivanov.apigateway.dto.response.ValidateAccessTokenResponseDto;
import ru.ivanov.apigateway.exception.ExternalServiceException;
import ru.ivanov.apigateway.service.AuthenticationService;

import java.net.URI;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final RestTemplate restTemplate;

    @Value("${client.authentication-service.url}")
    private String url;

    @Override
    public ResponseEntity<ValidateAccessTokenResponseDto> validate(String accessToken) {

        RequestEntity<?> requestEntity = RequestEntity.method(HttpMethod.POST, URI.create(url))
                .body(new AccessTokenRequestDto(accessToken));
        ResponseEntity<ValidateAccessTokenResponseDto> response;
        try {
            response = restTemplate.exchange(requestEntity, new ParameterizedTypeReference<>() {
            });
        } catch (Exception e) {
            final String REGEX_SPLIT_ERROR_MESSAGE = "\".\"";
            final String REGEX_FIND_CODE = "^\\d{3}";
            String[] errMessageArray = e.getMessage().split(REGEX_SPLIT_ERROR_MESSAGE);
            Matcher errorMatcher = Pattern.compile(REGEX_FIND_CODE).matcher(errMessageArray[0]);
            errorMatcher.find();
            throw new ExternalServiceException(
                    errMessageArray[errMessageArray.length - 1],
                    Integer.parseInt(errorMatcher.group())
            );
        }
        return response;
    }

}
