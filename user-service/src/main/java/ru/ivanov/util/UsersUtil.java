package ru.ivanov.util;

import lombok.experimental.UtilityClass;
import ru.ivanov.userservice.dto.request.UserRequestDto;
import ru.ivanov.userservice.dto.response.UserResponseDto;
import ru.ivanov.userservice.model.User;

@UtilityClass
public class UsersUtil {

    public static User updateFromTo(User user, UserRequestDto userRequestDto) {
        user.setName(userRequestDto.getName());
        user.setEmail(userRequestDto.getEmail().toLowerCase());
        user.setPassword(userRequestDto.getPassword());
        return user;
    }

    public static UserResponseDto getUserResponseDto(User user) {
        return UserResponseDto.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .registered(user.getRegistered())
                .build();
    }

}
