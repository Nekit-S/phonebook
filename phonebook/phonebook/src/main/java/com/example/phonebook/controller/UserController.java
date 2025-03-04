package com.example.phonebook.controller;

import com.example.phonebook.dto.UserDto;
import com.example.phonebook.dto.UserRequestDto;
import com.example.phonebook.mapper.UserMapper;
import com.example.phonebook.model.User;
import com.example.phonebook.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/users")
@Tag(name = "Пользователи", description = "API для управления пользователями телефонной книги")
public class UserController {
    
    private final UserService userService;
    private final UserMapper userMapper;

    @Autowired
    public UserController(UserService userService, UserMapper userMapper) {
        this.userService = userService;
        this.userMapper = userMapper;
    }

    @Operation(summary = "Создать нового пользователя", 
               description = "Создает нового пользователя в телефонной книге")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Пользователь успешно создан", 
                   content = @Content(schema = @Schema(implementation = UserDto.class))),
        @ApiResponse(responseCode = "409", description = "Email уже существует в системе", 
                   content = @Content),
        @ApiResponse(responseCode = "400", description = "Некорректные данные запроса", 
                   content = @Content)
    })
    @PostMapping
    public ResponseEntity<UserDto> addUser(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                description = "Данные нового пользователя", 
                required = true)
            @RequestBody UserRequestDto userRequestDto) {
        User user = userMapper.toEntity(userRequestDto);
        User savedUser = userService.addUser(user);
        return ResponseEntity.ok(userMapper.toDto(savedUser));
    }

    @Operation(summary = "Удалить пользователя", 
               description = "Удаляет пользователя по его идентификатору")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Пользователь успешно удален", 
                   content = @Content),
        @ApiResponse(responseCode = "404", description = "Пользователь не найден", 
                   content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(
            @Parameter(description = "Идентификатор пользователя", required = true)
            @PathVariable UUID id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Обновить данные пользователя", 
               description = "Обновляет данные существующего пользователя")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Данные пользователя успешно обновлены", 
                   content = @Content(schema = @Schema(implementation = UserDto.class))),
        @ApiResponse(responseCode = "404", description = "Пользователь не найден", 
                   content = @Content),
        @ApiResponse(responseCode = "409", description = "Email уже существует в системе", 
                   content = @Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<UserDto> updateUser(
            @Parameter(description = "Идентификатор пользователя", required = true)
            @PathVariable UUID id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                description = "Новые данные пользователя", 
                required = true)
            @RequestBody UserRequestDto userRequestDto) {
        User user = userMapper.toEntity(userRequestDto);
        User updatedUser = userService.updateUser(id, user);
        return ResponseEntity.ok(userMapper.toDto(updatedUser));
    }

    @Operation(summary = "Получить список всех пользователей", 
               description = "Возвращает страницу пользователей с возможностью пагинации и сортировки")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Список пользователей успешно получен", 
                   content = @Content(schema = @Schema(implementation = UserDto.class)))
    })
    @GetMapping
    public ResponseEntity<List<UserDto>> getAllUsers(
            @Parameter(description = "Параметры пагинации и сортировки")
            Pageable pageable) {
        Page<User> userPage = userService.getAllUsers(pageable);
        List<UserDto> userDtos = userPage.getContent().stream()
                .map(userMapper::toDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(userDtos);
    }

    @Operation(summary = "Получить пользователя по ID", 
               description = "Возвращает пользователя по его идентификатору")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Пользователь успешно найден", 
                   content = @Content(schema = @Schema(implementation = UserDto.class))),
        @ApiResponse(responseCode = "404", description = "Пользователь не найден", 
                   content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUserById(
            @Parameter(description = "Идентификатор пользователя", required = true)
            @PathVariable UUID id) {
        User user = userService.getUserById(id);
        return ResponseEntity.ok(userMapper.toDto(user));
    }
}