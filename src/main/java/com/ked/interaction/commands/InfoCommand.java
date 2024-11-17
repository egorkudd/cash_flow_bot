package com.ked.interaction.commands;

import com.ked.tg.utils.MessageUtil;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;

@Component
public class InfoCommand extends BotCommand {
    private final String MESSAGE = """
            Откройте меню в левом нижнем углу и выберите необходимую команду
            Перед добавлением доходов/расходов создайте категории, в которые будете их добавлять
             """;

    public InfoCommand() {
        super("info", "Information command");
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] strings) {
        sendInfoMessage(chat.getId(), absSender);
    }

    private void sendInfoMessage(long chatId, AbsSender absSender) {
        MessageUtil.sendMessage(chatId, MESSAGE, absSender);
    }
}
