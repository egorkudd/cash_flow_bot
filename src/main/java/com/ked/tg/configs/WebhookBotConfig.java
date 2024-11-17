package com.ked.tg.configs;

import com.ked.tg.bots.MyWebHookBot;
import com.ked.tg.controllers.WebhookController;
import com.ked.tg.services.UpdateHandleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.meta.api.methods.updates.SetWebhook;

@Slf4j
@Configuration
public class WebhookBotConfig {
    @Value("${telegram.bot.webhook_enabled}")
    private boolean isWebhookBotEnabled;

    @Value("${telegram.bot.name}")
    private String name;

    @Value("${telegram.bot.token}")
    private String token;

    @Value("${telegram.webhook-path}")
    private String webhookPath;

    @Bean
    public MyWebHookBot bot(UpdateHandleService updateHandleService) {
        if (isWebhookBotEnabled) {
            MyWebHookBot bot = new MyWebHookBot(token, SetWebhook.builder().url(webhookPath).build());
            bot.setBotPath(webhookPath);
            bot.setBotUsername(name);
            bot.setUpdateHandleService(updateHandleService);

            log.info("Bot initialized");
            return bot;
        }

        log.warn("Bot was not initialized");
        return null;
    }

    @Bean
    public WebhookController webhookController(MyWebHookBot bot) {
        if (isWebhookBotEnabled) {
            return new WebhookController(bot);
        }

        return null;
    }
}
