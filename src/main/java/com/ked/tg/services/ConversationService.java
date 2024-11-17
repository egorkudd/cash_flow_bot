package com.ked.tg.services;

import com.ked.interaction.enums.EConversation;
import com.ked.tg.enums.EMessage;
import com.ked.tg.exceptions.AbstractBotException;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.bots.AbsSender;

@Service
public interface ConversationService {
    void startConversation(long chatId, EConversation eConversation, AbsSender sender) throws AbstractBotException;

    void executeConversationStep(Update update, EMessage EMessage, AbsSender sender) throws AbstractBotException;
}
