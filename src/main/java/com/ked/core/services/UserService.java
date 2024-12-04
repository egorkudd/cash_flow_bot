package com.ked.core.services;

import com.ked.core.entities.User;
import com.ked.tg.exceptions.EntityNotFoundBotException;
import org.springframework.stereotype.Service;

@Service
public interface UserService {
    User findByTgId(Long tgId) throws EntityNotFoundBotException;

    boolean existsByTgId(Long tgId);

    User create(long chatId, String name);

    void changeUsername(String username, Long tgId) throws EntityNotFoundBotException;
}
