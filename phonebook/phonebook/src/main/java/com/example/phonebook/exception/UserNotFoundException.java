package com.example.phonebook.exception;

import java.util.UUID;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(UUID id) {
        super("User with ID " + id + " not found");
    }
}