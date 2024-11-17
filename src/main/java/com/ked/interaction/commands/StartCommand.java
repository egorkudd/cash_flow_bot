package com.ked.interaction.commands;

import com.ked.core.services.UserService;
import com.ked.tg.utils.MessageUtil;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;

@Component
public class StartCommand extends BotCommand {
    private final UserService userService;
    private static final String HELLO_MESSAGE = """
            Привет, дорогой друг!
            Я – бот!
            """;

    public StartCommand(UserService userService) {
        super("start", "Start command");
        this.userService = userService;
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] strings) {
        createUser(chat.getId(), user.getFirstName());
        sendHelloMessage(chat.getId(), absSender);
    }

    private void createUser(long chatId, String name) {
        if (!userService.existsByTgId(chatId)) {
            userService.create(chatId, name);
        }
    }

    private void sendHelloMessage(long chatId, AbsSender absSender) {
        MessageUtil.sendMessage(chatId, HELLO_MESSAGE, absSender);
    }
}
