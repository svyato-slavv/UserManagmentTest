package ru.ivanov.userservice.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.ivanov.userservice.dto.request.UserRequestDto;
import ru.ivanov.userservice.dto.response.UserResponseDto;
import ru.ivanov.userservice.model.User;
import ru.ivanov.userservice.service.UserService;
import ru.ivanov.util.UsersUtil;

import java.net.URI;

@RestController
@RequestMapping(value = UserController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class UserController {

    static final String REST_URL = "/api/v1/users";

    private final UserService userService;

    @GetMapping("/{id}")
    public UserResponseDto get(@PathVariable int id) {
        return UsersUtil.getUserResponseDto(userService.get(id));
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<UserResponseDto> createWithLocation(@Valid @RequestBody User user) {
        User created = userService.checkEmailAndSave(user);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/{id}")
                .buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(UsersUtil.getUserResponseDto(created));
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@Valid @RequestBody UserRequestDto userRequestDto, @PathVariable int id) {
        User user = userService.get(id);
        userService.checkEmailAndSave(UsersUtil.updateFromTo(user, userRequestDto));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable int id) {
        userService.delete(id);
    }

}
