package com.ked.tg.bots;

import com.ked.tg.services.UpdateHandleService;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

@Slf4j
public class MyLongPoolingBot extends TelegramLongPollingBot {
    @Getter
    private final String botUsername;
    private final UpdateHandleService updateHandleService;

    public MyLongPoolingBot(String botUsername, String token, UpdateHandleService updateHandleService) {
        super(token);
        this.botUsername = botUsername;
        this.updateHandleService = updateHandleService;
    }

    @Override
    public void onUpdateReceived(Update update) {
        updateHandleService.onUpdateReceived(update, this);
    }
}