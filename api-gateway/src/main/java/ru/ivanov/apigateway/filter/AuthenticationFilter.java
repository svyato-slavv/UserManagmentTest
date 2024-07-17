package ru.ivanov.apigateway.filter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import ru.ivanov.apigateway.dto.response.ValidateAccessTokenResponseDto;
import ru.ivanov.apigateway.exception.AccessTokenNotFoundException;
import ru.ivanov.apigateway.exception.ExternalServiceException;
import ru.ivanov.apigateway.exception.constant.ErrorMessages;
import ru.ivanov.apigateway.exception.dto.ErrorResponseDto;
import ru.ivanov.apigateway.service.AuthenticationService;

@Component
public class AuthenticationFilter extends AbstractGatewayFilterFactory<AuthenticationFilter.Config> {

    private final String HEADER_ADMIN_ID = "adminId";

    private final String BEARER_LABEL = "Bearer ";

    private final RouteValidator routeValidator;

    private final AuthenticationService authenticationService;

    public AuthenticationFilter(RouteValidator routeValidator, WebClient.Builder webClientBuilder, AuthenticationService authenticationService) {
        super(Config.class);
        this.routeValidator = routeValidator;
        this.authenticationService = authenticationService;
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            ServerHttpRequest request = null;
            if (routeValidator.isSecured.test(exchange.getRequest())) {
                if (!exchange.getRequest().getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
                    return handleErrorResponse(exchange, HttpStatus.FORBIDDEN, ErrorMessages.ACCESS_TOKEN_NOT_FOUND.getMessage());
                }
                String authHeader = exchange.getRequest().getHeaders().get(HttpHeaders.AUTHORIZATION).get(0);
                if (authHeader != null && authHeader.startsWith(BEARER_LABEL)) {
                    authHeader = authHeader.substring(7);
                }

                try {
                    ResponseEntity<ValidateAccessTokenResponseDto> validateResponse =
                            authenticationService.validate(authHeader);
                    if (validateResponse.getStatusCode().is2xxSuccessful()) {
                        ValidateAccessTokenResponseDto maybeValidate = validateResponse.getBody();
                        request = exchange.getRequest()
                                .mutate()
                                .header(HEADER_ADMIN_ID, maybeValidate.getAdminCredentialId())
                                .build();
                    }
                } catch (ExternalServiceException e) {
                    return handleErrorResponse(exchange, HttpStatus.valueOf(e.getCode()), e.getMessage());
                } catch (AccessTokenNotFoundException e) {
                    return handleErrorResponse(exchange, HttpStatus.FORBIDDEN, e.getMessage());
                } catch (Exception e) {
                    return handleErrorResponse(exchange, HttpStatus.INTERNAL_SERVER_ERROR, ErrorMessages.INTERNAL_SERVER_ERROR.getMessage());
                }
            }
            return chain.filter(exchange.mutate().request(request).build());
        };
    }

    private Mono<Void> handleErrorResponse(ServerWebExchange exchange, HttpStatus status, String errorMessage) {
        exchange.getResponse().setStatusCode(status);
        exchange.getResponse().getHeaders().add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        ErrorResponseDto errorResponseDto = new ErrorResponseDto(errorMessage);
        byte[] response;
        try {
            response = new ObjectMapper().writeValueAsBytes(errorResponseDto);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        DataBuffer buffer = exchange.getResponse().bufferFactory().wrap(response);
        return exchange.getResponse().writeWith(Mono.just(buffer));
    }

    public static class Config {

    }

}