package ru.ivanov.userservice.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import ru.ivanov.userservice.dto.response.UserResponseDto;
import ru.ivanov.userservice.exception.EmailAlreadyTakenException;
import ru.ivanov.userservice.exception.NotFoundException;
import ru.ivanov.userservice.model.User;
import ru.ivanov.userservice.service.impl.UserServiceImpl;

import java.util.Date;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper mapper;

    @MockBean
    UserServiceImpl userService;

    User user1 = new User(1, "test1", "test1@mail.ru", "test1", new Date());

    UserResponseDto userResponseDto1 = new UserResponseDto(1, "test1", "test1@mail.ru", new Date());

    Integer existingId = 1;

    Integer notExistingId = 3;
    User newUserCorrect = new User(2, "test2", "test2@mail.ru", "test2", new Date());

    User newUserWithBusyEmail = new User(2, "test2", "test1@mail.ru", "test2", new Date());


    @Test
    public void findById() throws Exception {
        when(userService.get(existingId)).thenReturn(user1);

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
                        .get(UserController.REST_URL + "/" + existingId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        String actual = mvcResult.getResponse().getContentAsString();
        String expected = mapper.writeValueAsString(userResponseDto1);
        Assertions.assertEquals(actual, expected);
    }

    @Test
    public void findByIdShouldReturnNotFound() throws Exception {
        when(userService.get(notExistingId)).thenThrow(NotFoundException.class);

        mockMvc.perform(MockMvcRequestBuilders
                        .get(UserController.REST_URL + "/" + notExistingId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void create() throws Exception {
        when(userService.checkEmailAndSave(any(User.class))).thenReturn(newUserCorrect);
        mockMvc.perform(MockMvcRequestBuilders
                        .post(UserController.REST_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(newUserCorrect)))
                .andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").exists());

        verify(userService).checkEmailAndSave(any(User.class));
    }

    @Test
    public void createShouldThrowEmailAlreadyTakenException() throws Exception {
        doThrow(EmailAlreadyTakenException.class).when(userService).checkEmailAndSave(any(User.class));
        mockMvc.perform(MockMvcRequestBuilders
                        .post(UserController.REST_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(newUserWithBusyEmail)))
                .andExpect(status().isConflict());
    }

    @Test
    public void updateShouldReturnNotFound() throws Exception {
        when(userService.get(any(Integer.class))).thenThrow(NotFoundException.class);

        mockMvc.perform(MockMvcRequestBuilders
                        .put(UserController.REST_URL + "/" + notExistingId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(user1)))
                .andExpect(status().isNotFound());
    }

    @Test
    public void update() throws Exception {
        when(userService.get(any(Integer.class))).thenReturn(user1);

        mockMvc.perform(MockMvcRequestBuilders
                        .put(UserController.REST_URL + "/" + existingId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(user1)))
                .andExpect(status().isNoContent());

        verify(userService).get(any(Integer.class));
        verify(userService).checkEmailAndSave(any(User.class));
    }

    @Test
    public void delete() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .delete(UserController.REST_URL + "/" + existingId))
                .andExpect(status().isNoContent());

        verify(userService).delete(any(Integer.class));
    }

    @Test
    public void deleteShouldReturnNotFound() throws Exception {
        doThrow(NotFoundException.class).when(userService).delete(notExistingId);

        mockMvc.perform(MockMvcRequestBuilders
                        .delete(UserController.REST_URL + "/" + notExistingId))
                .andExpect(status().isNotFound());

        verify(userService).delete(any(Integer.class));
    }

}