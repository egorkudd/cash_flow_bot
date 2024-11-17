package com.ked.tg.services;

import com.ked.interaction.enums.EConversation;
import com.ked.interaction.enums.EConversationStep;
import com.ked.tg.entities.TgChat;
import org.springframework.stereotype.Service;

@Service
public interface TgChatService {
    TgChat getChatById(long chatId);

    TgChat createChatHash(long chatId, EConversation eConversation, EConversationStep eConversationStep);

    void save(TgChat tgChat);

    void delete(TgChat tgChat);
}
