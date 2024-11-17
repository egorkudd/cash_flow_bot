package com.ked.tg.mappers;

import com.ked.interaction.enums.EConversation;
import com.ked.interaction.enums.EConversationStep;
import com.ked.tg.entities.TgChat;
import org.springframework.stereotype.Component;

@Component
public class TgChatMapper {
    private static final int DEFAULT_MESSAGE_ID = -1;
    private static final int DEFAULT_PAGE_NUMBER = 0;

    public TgChat chatHash(
            long chatId, EConversation eConversation, EConversationStep eConversationStep
    ) {
        TgChat tgChat = new TgChat();
        tgChat.setChatId(chatId);
        tgChat.setEConversation(eConversation);
        tgChat.setEConversationStep(eConversationStep);
        tgChat.setPrevBotMessageId(DEFAULT_MESSAGE_ID);
        tgChat.setPrevBotMessagePageNumber(DEFAULT_PAGE_NUMBER);
        return tgChat;
    }
}
