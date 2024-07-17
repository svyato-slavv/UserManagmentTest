package ru.ivanov.apigateway.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccessTokenRequestDto {

    private String accessToken;

}
