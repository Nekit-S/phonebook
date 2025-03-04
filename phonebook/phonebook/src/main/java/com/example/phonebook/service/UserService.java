package com.example.phonebook.service;

import com.example.phonebook.model.User;
import com.example.phonebook.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class UserService {
    
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Создает нового пользователя в системе
     * @param user Объект пользователя для создания (без ID)
     * @return Созданный пользователь с присвоенным ID
     */
    @Transactional
    public User addUser(User user) {
        // Больше не генерируем UUID вручную - это будет делать Hibernate
        return userRepository.save(user);
    }

    /**
     * Удаляет пользователя по ID
     * @param id UUID пользователя для удаления
     * @throws IllegalArgumentException если пользователь не найден
     */
    @Transactional
    public void deleteUser(UUID id) {
        // Проверяем существование пользователя перед удалением
        if (!userRepository.existsById(id)) {
            throw new IllegalArgumentException("User with ID " + id + " not found");
        }
        userRepository.deleteById(id);
    }

    /**
     * Обновляет данные существующего пользователя
     * @param id UUID пользователя для обновления
     * @param newUserData Новые данные пользователя
     * @return Обновленный объект пользователя
     * @throws IllegalArgumentException если пользователь не найден
     */
    @Transactional
    public User updateUser(UUID id, User newUserData) {
        // Находим существующего пользователя
        User existingUser = userRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + id));
        
        // Обновляем только разрешенные поля
        existingUser.setName(newUserData.getName());
        existingUser.setEmail(newUserData.getEmail());
        
        return userRepository.save(existingUser);
    }

    /**
     * Получает список всех пользователей с пагинацией
     * @param pageable Информация о пагинации
     * @return Страница пользователей
     */
    @Transactional(readOnly = true)
    public Page<User> getAllUsers(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    /**
     * Получает список всех пользователей (устаревший метод)
     * @return Список пользователей
     */
    @Transactional(readOnly = true)
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    /**
     * Находит пользователя по ID
     * @param id UUID пользователя
     * @return Найденный пользователь
     * @throws IllegalArgumentException если пользователь не найден
     */
    @Transactional(readOnly = true)
    public User getUserById(UUID id) {
        return userRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + id));
    }
}