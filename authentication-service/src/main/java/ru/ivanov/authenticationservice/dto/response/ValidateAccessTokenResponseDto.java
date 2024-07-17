package ru.ivanov.authenticationservice.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class ValidateAccessTokenResponseDto {

    private Integer adminCredentialId;
}
