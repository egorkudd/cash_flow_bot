package com.ked.core.services.impl;

import com.ked.core.entities.User;
import com.ked.core.repositories.UserRepository;
import com.ked.core.services.UserService;
import com.ked.tg.exceptions.EntityNotFoundBotException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

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
}
