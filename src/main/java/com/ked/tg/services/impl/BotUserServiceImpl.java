package com.ked.tg.services.impl;

import com.ked.tg.entities.BotUser;
import com.ked.tg.enums.ERole;
import com.ked.tg.exceptions.EntityNotFoundBotException;
import com.ked.tg.repositories.BotUserRepository;
import com.ked.tg.repositories.RoleRepository;
import com.ked.tg.services.BotUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BotUserServiceImpl implements BotUserService {
    private final BotUserRepository botUserRepository;
    private final RoleRepository roleRepository;

    @Override
    public BotUser getById(long botUserId) throws EntityNotFoundBotException {
        return botUserRepository.findById(botUserId)
                .orElseThrow(() -> getException(ERole.ROLE_USER, botUserId));
    }

    @Override
    public boolean existsByTgId(long tgId) {
        return botUserRepository.existsByTgId(tgId);
    }

    @Override
    public BotUser getByChatIdAndRole(
            long chatId, ERole eRole
    ) throws EntityNotFoundBotException {
        BotUser botUser = botUserRepository.findByTgId(chatId)
                .orElseThrow(() -> getException(eRole, chatId));

        if (!hasRole(botUser, eRole)) {
            throw getException(eRole, chatId);
        }

        return botUser;
    }

    @Override
    public boolean hasRole(BotUser botUser, ERole eRole) {
        return botUser.getRoleList().stream().anyMatch(role -> role.getRoleName().equals(eRole));
    }

    @Override
    public void flush() {
        botUserRepository.flush();
    }

    @Override
    public List<BotUser> findAll() {
        return botUserRepository.findAll();
    }

    @Override
    public void create(long chatId, String name) {
        botUserRepository.saveAndFlush(new BotUser(chatId, name));
    }

    private EntityNotFoundBotException getException(ERole eRole, long chatId) {
        return new EntityNotFoundBotException("Не существует %s с ID = %d".formatted(eRole, chatId));
    }
}
