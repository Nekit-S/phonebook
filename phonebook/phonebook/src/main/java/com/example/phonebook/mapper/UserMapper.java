package com.example.phonebook.mapper;

import com.example.phonebook.dto.UserDto;
import com.example.phonebook.dto.UserRequestDto;
import com.example.phonebook.model.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public UserDto toDto(User user) {
        return new UserDto(
            user.getId(),
            user.getName(),
            user.getEmail()
        );
    }

    public User toEntity(UserRequestDto userRequestDto) {
        User user = new User();
        user.setName(userRequestDto.getName());
        user.setEmail(userRequestDto.getEmail());
        return user;
    }

    public void updateEntityFromDto(UserRequestDto userRequestDto, User user) {
        user.setName(userRequestDto.getName());
        user.setEmail(userRequestDto.getEmail());
    }
}