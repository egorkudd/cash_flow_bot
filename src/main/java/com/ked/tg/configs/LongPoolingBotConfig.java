package com.ked.tg.configs;

import com.ked.tg.bots.MyLongPoolingBot;
import com.ked.tg.services.UpdateHandleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@Slf4j
@Configuration
public class LongPoolingBotConfig {
    @Value("${telegram.bot.long_pooling_enabled}")
    private boolean isBotEnabled;

    @Value("${telegram.bot.name}")
    private String name;

    @Value("${telegram.bot.token}")
    private String token;


    @Bean
    public MyLongPoolingBot tgLongPoolingBot(@Autowired UpdateHandleService updateHandleService) {
        if (isBotEnabled) {
            MyLongPoolingBot myLongPoolingBot = new MyLongPoolingBot(name, token, updateHandleService);
            log.info("Bot initialized");
            return myLongPoolingBot;
        }

        log.warn("Bot was not initialized");
        return null;
    }

    @Bean
    public TelegramBotsApi telegramBotsApi(MyLongPoolingBot myTelegramTGLongPoolingBot) throws TelegramApiException {
        TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
        if (isBotEnabled) {
            botsApi.registerBot(myTelegramTGLongPoolingBot);
        }

        return botsApi;
    }
}
