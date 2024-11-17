package com.ked.interaction.commands;

import com.ked.interaction.enums.EConversation;
import com.ked.tg.exceptions.AbstractBotException;
import com.ked.tg.services.ConversationService;
import com.ked.tg.utils.MessageUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;

@Slf4j
@Component
public class ConfigureCommand extends BotCommand {
    private final ConversationService conversationService;

    public ConfigureCommand(ConversationService conversationService) {
        super("configure", "Configure categories and something else");
        this.conversationService = conversationService;
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] arguments) {
        try {
            startConversation(chat.getId(), absSender);
        } catch (AbstractBotException e) {
            MessageUtil.sendMessage(chat.getId(), e.getUserMessage(), absSender);
            log.error(e.getMessage());
        }
    }

    private void startConversation(Long chatId, AbsSender absSender) {
        conversationService.startConversation(chatId, EConversation.CONFIGURE, absSender);
    }
}
//    TODO: создать диалог
    /*
    выбрать, что изменить (имя, категории)
    имя
        ввести имя
    категории
        выберите удалить/добавить
        добавить:
            введите название (проверить, нет ли такого уже)
        удалить:
            выбрать название кнопкой
            спросить, куда перенести транзакции
                удалить
                выбрать другую категорию
     */
//    TODO: дать возможность выйти из диалога
