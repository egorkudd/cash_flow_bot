package com.ked.tg.services;

import com.ked.tg.entities.BotMessage;
import com.ked.tg.entities.BotMessageButton;
import com.ked.tg.enums.EChat;
import com.ked.tg.exceptions.EntityNotFoundBotException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface BotMessageService {
    void create(long botUserId);

    BotMessage getProcessedMessageByUserId(long botUserId) throws EntityNotFoundBotException;

    void saveText(long botUserId, String text) throws EntityNotFoundBotException;

    void saveSentStatus(BotMessage botMessage);

    void delete(BotMessage botMessage);

    void createButton(long botUserId, String buttonName, String buttonLink) throws EntityNotFoundBotException;

    List<BotMessageButton> getButtonList(BotMessage botMessage);

    void deleteButtons(BotMessage botMessage);

    void saveChatType(long userId, EChat eChat) throws EntityNotFoundBotException;
}
