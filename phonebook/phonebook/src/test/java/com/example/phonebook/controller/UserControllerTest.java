package com.example.phonebook.controller;

import com.example.phonebook.dto.UserDto;
import com.example.phonebook.dto.UserRequestDto;
import com.example.phonebook.mapper.UserMapper;
import com.example.phonebook.model.User;
import com.example.phonebook.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class UserControllerTest {

    private MockMvc mockMvc;

    @Mock
    private UserService userService;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserController userController;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        // Добавляем PageableHandlerMethodArgumentResolver для поддержки Pageable
        mockMvc = MockMvcBuilders.standaloneSetup(userController)
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void testAddUser() throws Exception {
        // Создаем DTO запроса
        UserRequestDto requestDto = new UserRequestDto();
        requestDto.setName("Иван Иванов");
        requestDto.setEmail("ivan@example.com");

        // Создаем сущность
        User user = new User();
        user.setName("Иван Иванов");
        user.setEmail("ivan@example.com");
        
        // Создаем DTO ответа
        UserDto responseDto = new UserDto();
        responseDto.setName("Иван Иванов");
        responseDto.setEmail("ivan@example.com");

        // Настраиваем моки
        when(userMapper.toEntity(any(UserRequestDto.class))).thenReturn(user);
        when(userService.addUser(any(User.class))).thenReturn(user);
        when(userMapper.toDto(any(User.class))).thenReturn(responseDto);

        // Выполняем запрос
        mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Иван Иванов"))
                .andExpect(jsonPath("$.email").value("ivan@example.com"));

        // Проверяем, что сервис был вызван
        verify(userService, times(1)).addUser(any(User.class));
    }

    @Test
    void testGetAllUsers() throws Exception {
        // Создаем сущности
        User user1 = new User();
        user1.setName("Иван Иванов");
        user1.setEmail("ivan@example.com");

        User user2 = new User();
        user2.setName("Мария Сидорова");
        user2.setEmail("maria@example.com");

        List<User> users = Arrays.asList(user1, user2);
        Page<User> userPage = new PageImpl<>(users);

        // Создаем DTO
        UserDto dto1 = new UserDto();
        dto1.setName("Иван Иванов");
        dto1.setEmail("ivan@example.com");

        UserDto dto2 = new UserDto();
        dto2.setName("Мария Сидорова");
        dto2.setEmail("maria@example.com");

        // Настраиваем моки
        when(userService.getAllUsers(any(Pageable.class))).thenReturn(userPage);
        when(userMapper.toDto(user1)).thenReturn(dto1);
        when(userMapper.toDto(user2)).thenReturn(dto2);

        // Выполняем запрос
        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Иван Иванов"))
                .andExpect(jsonPath("$[1].name").value("Мария Сидорова"));

        // Проверяем, что сервис был вызван
        verify(userService, times(1)).getAllUsers(any(Pageable.class));
    }

    @Test
    void testGetUserById() throws Exception {
        // Создаем UUID
        UUID userId = UUID.randomUUID();

        // Создаем сущность
        User user = new User();
        user.setId(userId);
        user.setName("Иван Иванов");
        user.setEmail("ivan@example.com");

        // Создаем DTO
        UserDto dto = new UserDto();
        dto.setId(userId);
        dto.setName("Иван Иванов");
        dto.setEmail("ivan@example.com");

        // Настраиваем моки
        when(userService.getUserById(userId)).thenReturn(user);
        when(userMapper.toDto(user)).thenReturn(dto);

        // Выполняем запрос
        mockMvc.perform(get("/users/{id}", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Иван Иванов"))
                .andExpect(jsonPath("$.email").value("ivan@example.com"));

        // Проверяем, что сервис был вызван
        verify(userService, times(1)).getUserById(userId);
    }

    @Test
    void testUpdateUser() throws Exception {
        // Создаем UUID
        UUID userId = UUID.randomUUID();

        // Создаем DTO запроса
        UserRequestDto requestDto = new UserRequestDto();
        requestDto.setName("Иван Петров");
        requestDto.setEmail("ivan.petrov@example.com");

        // Создаем сущность
        User user = new User();
        user.setName("Иван Петров");
        user.setEmail("ivan.petrov@example.com");

        // Создаем DTO ответа
        UserDto responseDto = new UserDto();
        responseDto.setId(userId);
        responseDto.setName("Иван Петров");
        responseDto.setEmail("ivan.petrov@example.com");

        // Настраиваем моки
        when(userMapper.toEntity(any(UserRequestDto.class))).thenReturn(user);
        when(userService.updateUser(eq(userId), any(User.class))).thenReturn(user);
        when(userMapper.toDto(any(User.class))).thenReturn(responseDto);

        // Выполняем запрос
        mockMvc.perform(put("/users/{id}", userId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Иван Петров"))
                .andExpect(jsonPath("$.email").value("ivan.petrov@example.com"));

        // Проверяем, что сервис был вызван
        verify(userService, times(1)).updateUser(eq(userId), any(User.class));
    }

    @Test
    void testDeleteUser() throws Exception {
        // Создаем UUID
        UUID userId = UUID.randomUUID();

        // Настраиваем мок
        doNothing().when(userService).deleteUser(userId);

        // Выполняем запрос
        mockMvc.perform(delete("/users/{id}", userId))
                .andExpect(status().isNoContent());

        // Проверяем, что сервис был вызван
        verify(userService, times(1)).deleteUser(userId);
    }
}