package com.ked.core.services.impl;

import com.ked.core.entities.User;
import com.ked.core.repositories.UserRepository;
import com.ked.core.services.UserService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public User findByTgId(Long tgId) {
        return userRepository.findByTgId(tgId).orElseThrow(() ->
                new EntityNotFoundException("Не существует пользователя TG ID=".concat(String.valueOf(tgId)))
        );
    }

    @Override
    public boolean existsByTgId(Long tgId) {
        return userRepository.existsByTgId(tgId);
    }

    @Override
    public void create(long chatId, String name) {
        userRepository.saveAndFlush(new User(chatId, name));
    }
}
