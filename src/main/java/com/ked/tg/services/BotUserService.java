package com.ked.tg.services;

import com.ked.tg.entities.BotUser;
import com.ked.tg.enums.ERole;
import com.ked.tg.exceptions.EntityNotFoundBotException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface BotUserService {
    BotUser getById(long botUserId);

    boolean existsByTgId(long tgId);

    BotUser getByChatIdAndRole(long chatId, ERole eRole) throws EntityNotFoundBotException;

    boolean hasRole(BotUser botUser, ERole eRole);

    void flush();

    List<BotUser> findAll();

    void create(long chatId, String name);
}
