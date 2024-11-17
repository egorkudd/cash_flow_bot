package com.ked.tg.services;

import com.ked.tg.exceptions.AbstractBotException;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.bots.AbsSender;

@Service
public interface UpdateHandleService {
    void onUpdateReceived(Update update, AbsSender sender);

    void handleCallbackRequest(Update update, AbsSender sender) throws AbstractBotException;

    void handleMessageRequest(Update update, AbsSender sender) throws AbstractBotException;

    void handleMyChatMember(Update update);
}
