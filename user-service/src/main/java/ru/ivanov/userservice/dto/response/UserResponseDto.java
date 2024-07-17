package ru.ivanov.userservice.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponseDto {

    private Integer id;

    private String name;

    private String email;

    private Date registered;

}
