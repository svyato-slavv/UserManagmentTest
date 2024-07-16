package ru.ivanov.userservice.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.ivanov.userservice.exception.EmailAlreadyTakenException;
import ru.ivanov.userservice.exception.constant.ErrorMessages;
import ru.ivanov.userservice.model.User;
import ru.ivanov.userservice.repository.UserRepository;
import ru.ivanov.userservice.service.UserService;

import static ru.ivanov.userservice.config.PasswordEncoderConfig.PASSWORD_ENCODER;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository repository;

    @Override
    public User get(int id) {
        return repository.getExistedUser(id);
    }

    @Transactional
    @Override
    public User checkEmailAndSave(User user) {
        if (isEmailAlreadyTaken(user)) {
            throw new EmailAlreadyTakenException(ErrorMessages.EMAIL_ALREADY_TAKEN.getMessage());
        }
        return prepareAndSave(user);
    }

    @Transactional
    @Override
    public void delete(int id) {
        repository.deleteExistedUser(id);
    }

    private User prepareAndSave(User user) {
        user.setPassword(PASSWORD_ENCODER.encode(user.getPassword()));
        user.setEmail(user.getEmail().toLowerCase());
        return repository.save(user);
    }

    private boolean isEmailAlreadyTaken(User user) {
        return repository.findByEmailIgnoreCase(user.getEmail())
                .isPresent();
    }

}
