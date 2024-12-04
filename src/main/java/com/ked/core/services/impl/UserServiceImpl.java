package com.ked.core.services.impl;

import com.ked.core.entities.User;
import com.ked.core.repositories.UserRepository;
import com.ked.core.services.UserService;
import com.ked.tg.exceptions.EntityNotFoundBotException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    @Override
    public User findByTgId(Long tgId) throws EntityNotFoundException {
        return userRepository.findByTgId(tgId).orElseThrow(() ->
                new EntityNotFoundException("Не существует пользователя TG ID=".concat(String.valueOf(tgId)))
        );
    }

    @Override
    public boolean existsByTgId(Long tgId) {
        return userRepository.existsByTgId(tgId);
    }

    @Override
    public User create(long chatId, String name) {
        return userRepository.saveAndFlush(new User(chatId, name));
    }

    @Override
    public void changeUsername(String username, Long tgId) throws EntityNotFoundBotException {
        User user = findByTgId(tgId);
        user.setName(username);
        userRepository.saveAndFlush(user);
    }

    @Override
    public void setEmail(String email, Long tgId) throws EntityNotFoundBotException {
        User user = findByTgId(tgId);
        user.setEmail(email);
        userRepository.saveAndFlush(user);
    }

    @Override
    public void setPassword(String password, Long tgId) throws EntityNotFoundBotException {
        User user = findByTgId(tgId);
        user.setPasswordHash(passwordEncoder.encode(password));
        userRepository.saveAndFlush(user);
    }

    @Override
    public User findById(Long userId) throws EntityNotFoundBotException {
        return userRepository.findById(userId).orElseThrow(() ->
                new EntityNotFoundException("Не существует пользователя ID=".concat(String.valueOf(userId)))
        );
    }

    @Override
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }
}
