package ru.ivanov.authenticationservice.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class JwtRequestDto {

    private String login;

    private String password;

}
