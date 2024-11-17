package com.ked.core.services;

import com.ked.core.entities.User;
import org.springframework.stereotype.Service;

@Service
public interface UserService {
    User findByTgId(Long tgId);

    boolean existsByTgId(Long tgId);


    void create(long chatId, String name);
}
