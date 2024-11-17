package com.ked.tg.controllers;

import com.ked.tg.bots.MyWebHookBot;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.telegram.telegrambots.meta.api.objects.Update;

@RestController
@RequiredArgsConstructor
public class WebhookController {
    private final MyWebHookBot bot;

    @PostMapping("/")
    public void onUpdateReceived(@RequestBody Update update) {
        bot.onWebhookUpdateReceived(update);
    }
}