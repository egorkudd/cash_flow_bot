package com.ked.tg.services.impl;

import com.ked.interaction.enums.EConversation;
import com.ked.interaction.enums.EConversationStep;
import com.ked.tg.entities.TgChat;
import com.ked.tg.mappers.TgChatMapper;
import com.ked.tg.repositories.TgChatRepository;
import com.ked.tg.services.TgChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TgChatServiceImpl implements TgChatService {
    private final TgChatRepository tgChatRepository;
    private final TgChatMapper tgChatMapper;

    @Override
    public TgChat getChatById(long chatId) {
        return tgChatRepository.findByChatId(chatId).orElse(null);
    }

    @Override
    public TgChat createChatHash(
            long chatId, EConversation eConversation, EConversationStep eConversationStep
    ) {
        return tgChatMapper.chatHash(chatId, eConversation, eConversationStep);
    }

    @Override
    public void save(TgChat tgChat) {
        tgChatRepository.save(tgChat);
    }

    @Override
    public void delete(TgChat tgChat) {
        tgChatRepository.deleteById(tgChat.getId());
    }
}
