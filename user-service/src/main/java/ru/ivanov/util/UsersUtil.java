package ru.ivanov.util;

import lombok.experimental.UtilityClass;
import ru.ivanov.userservice.dto.request.UserTo;
import ru.ivanov.userservice.model.User;

@UtilityClass
public class UsersUtil {

    public static User updateFromTo(User user, UserTo userTo) {
        user.setName(userTo.getName());
        user.setEmail(userTo.getEmail().toLowerCase());
        user.setPassword(userTo.getPassword());
        return user;
    }

}
