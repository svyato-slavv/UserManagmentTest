package ru.ivanov.userservice.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserTo {

    @NotBlank
    @Size(min = 2, max = 128)
    private String name;

    @Email
    @NotBlank
    @Size(max = 128)
    String email;

    @NotBlank
    @Size(max = 128)
    String password;

}
