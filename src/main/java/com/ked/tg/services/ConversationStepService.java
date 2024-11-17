package com.ked.tg.services;

import com.ked.interaction.enums.EConversation;
import com.ked.interaction.enums.EConversationStep;
import com.ked.tg.dto.MessageDto;
import com.ked.tg.entities.TgChat;
import com.ked.tg.exceptions.AbstractBotException;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.bots.AbsSender;

@Service
public interface ConversationStepService {
    EConversationStep getStartStep(EConversation eConversation);

    void prepareStep(TgChat tgChat, AbsSender sender) throws AbstractBotException;

    EConversationStep executeStep(TgChat tgChat, MessageDto messageDto, AbsSender sender) throws AbstractBotException;

    String getFinishMessageText(EConversation eConversation);
}
