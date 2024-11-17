package com.ked.tg.bots;

import com.ked.tg.services.UpdateHandleService;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.updates.SetWebhook;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.starter.SpringWebhookBot;

@Slf4j
@Getter
@Setter
public final class MyWebHookBot extends SpringWebhookBot {
    private String botUsername;
    private String botPath;
    private UpdateHandleService updateHandleService;

    public MyWebHookBot(String token, SetWebhook setWebhook) {
        super(setWebhook, token);
    }

    @Override
    public BotApiMethod<?> onWebhookUpdateReceived(Update update) {
        updateHandleService.onUpdateReceived(update, this);
        return null;
    }
}
