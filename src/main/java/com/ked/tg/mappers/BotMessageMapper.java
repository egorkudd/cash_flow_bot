package com.ked.tg.mappers;

import com.ked.tg.entities.BotMessage;
import com.ked.tg.enums.EBotMessage;
import org.springframework.stereotype.Component;

@Component
public class BotMessageMapper {
    public BotMessage botMessage(long writerId) {
        BotMessage botMessage = new BotMessage();
        botMessage.setWriterId(writerId);
        botMessage.setStatus(EBotMessage.WRITING);
        return botMessage;
    }
}
