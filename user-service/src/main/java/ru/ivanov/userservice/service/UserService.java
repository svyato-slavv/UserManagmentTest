package ru.ivanov.userservice.service;

import ru.ivanov.userservice.model.User;

public interface UserService {

    User get(int id);

    User checkEmailAndSave(User user);

    void delete(int id);

}
