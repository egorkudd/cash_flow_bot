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
            ⚙️ Возможности бота
            ✅ В разделе /configure вы можете:
                1) Добавить или переименовать категории доходов/трат
                2) Изменить своё имя
                3) Получить доступ к сайту
            ✅ В разделе /add_transaction вы можете:
                1) Добавить доход или расход
            ✅ В разделе /statistic в можете:
                1) Получить статистику доходов и расходов
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
