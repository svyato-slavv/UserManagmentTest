package ru.ivanov.userservice.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRequestDto {

    @NotBlank
    @Size(min = 2, max = 128)
    private String name;

    @Email
    @NotBlank
    @Size(max = 128)
    private String email;

    @NotBlank
    @Size(max = 128)
    private String password;

}
