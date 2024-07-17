package ru.ivanov.authenticationservice.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RefreshJwtRequestDto {

    public String refreshToken;

}
