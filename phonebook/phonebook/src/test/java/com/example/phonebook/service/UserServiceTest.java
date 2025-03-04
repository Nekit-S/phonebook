package com.example.phonebook.service;

import com.example.phonebook.model.User;
import com.example.phonebook.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAddUser() {
        // Подготовка данных
        User user = new User();
        user.setName("Иван Иванов");
        user.setEmail("ivan@example.com");

        when(userRepository.save(user)).thenReturn(user);

        // Вызов метода
        User savedUser = userService.addUser(user);

        // Проверка результата
        assertNotNull(savedUser);
        assertEquals("Иван Иванов", savedUser.getName());
        assertEquals("ivan@example.com", savedUser.getEmail());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void testDeleteUser() {
        // Подготовка данных
        UUID userId = UUID.randomUUID();
    
        // Настраиваем мокирование
        when(userRepository.existsById(userId)).thenReturn(true);
        doNothing().when(userRepository).deleteById(userId);
    
        // Вызов метода
        userService.deleteUser(userId);
    
        // Проверки
        verify(userRepository, times(1)).existsById(userId);
        verify(userRepository, times(1)).deleteById(userId);
    }

    @Test
    void testDeleteUser_ThrowsExceptionWhenUserNotFound() {
        // Подготовка данных
        UUID userId = UUID.randomUUID();
    
        // Настраиваем мокирование - пользователь не существует
        when(userRepository.existsById(userId)).thenReturn(false);
    
        // Вызов метода и проверка исключения
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class, 
            () -> userService.deleteUser(userId)
        );
        
        assertTrue(exception.getMessage().contains("not found"));
        verify(userRepository, times(1)).existsById(userId);
        verify(userRepository, never()).deleteById(userId);
    }

    @Test
    void testUpdateUser() {
        // Подготовка данных
        UUID userId = UUID.randomUUID();

        // Существующий пользователь в базе
        User existingUser = new User();
        existingUser.setId(userId);
        existingUser.setName("Иван Иванов");
        existingUser.setEmail("ivan@example.com");

        // Новые данные для обновления
        User updatedUserData = new User();
        updatedUserData.setName("Иван Петров");
        updatedUserData.setEmail("ivan.petrov@example.com");

        // Мокируем вызовы репозитория
        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));
        when(userRepository.save(existingUser)).thenReturn(existingUser);

        // Вызов метода
        User result = userService.updateUser(userId, updatedUserData);

        // Проверки
        assertNotNull(result, "Метод должен вернуть обновленного пользователя");
        assertEquals("Иван Петров", result.getName(), "Имя пользователя должно быть обновлено");
        assertEquals("ivan.petrov@example.com", result.getEmail(), "Email пользователя должен быть обновлен");

        // Проверка, что методы репозитория вызваны корректно
        verify(userRepository, times(1)).findById(userId);
        verify(userRepository, times(1)).save(existingUser);
    }

    @Test
    void testGetAllUsers() {
        // Подготовка данных
        User user1 = new User();
        user1.setName("Иван Иванов");
        user1.setEmail("ivan@example.com");

        User user2 = new User();
        user2.setName("Мария Сидорова");
        user2.setEmail("maria@example.com");

        when(userRepository.findAll()).thenReturn(Arrays.asList(user1, user2));

        // Вызов метода
        List<User> users = userService.getAllUsers();

        // Проверка результата
        assertEquals(2, users.size());
        verify(userRepository, times(1)).findAll();
    }

    @Test
    void testGetAllUsersWithPagination() {
        // Подготовка данных
        User user1 = new User();
        user1.setName("Иван Иванов");
        user1.setEmail("ivan@example.com");

        User user2 = new User();
        user2.setName("Мария Сидорова");
        user2.setEmail("maria@example.com");

        List<User> userList = Arrays.asList(user1, user2);
        Page<User> userPage = new PageImpl<>(userList);
        
        Pageable pageable = mock(Pageable.class);
        when(userRepository.findAll(pageable)).thenReturn(userPage);

        // Вызов метода
        Page<User> result = userService.getAllUsers(pageable);

        // Проверка результата
        assertEquals(2, result.getContent().size());
        verify(userRepository, times(1)).findAll(pageable);
    }

    @Test
    void testGetUserById() {
        // Подготовка данных
        UUID userId = UUID.randomUUID();
        User user = new User();
        user.setId(userId);
        user.setName("Иван Иванов");
        user.setEmail("ivan@example.com");

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        // Вызов метода
        User result = userService.getUserById(userId);

        // Проверка результата
        assertNotNull(result);
        assertEquals("Иван Иванов", result.getName());
        assertEquals("ivan@example.com", result.getEmail());
        verify(userRepository, times(1)).findById(userId);
    }

    @Test
    void testGetUserById_ThrowsExceptionWhenUserNotFound() {
        // Подготовка данных
        UUID userId = UUID.randomUUID();
        
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // Вызов метода и проверка исключения
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class, 
            () -> userService.getUserById(userId)
        );
        
        assertTrue(exception.getMessage().contains("not found"));
        verify(userRepository, times(1)).findById(userId);
    }
}