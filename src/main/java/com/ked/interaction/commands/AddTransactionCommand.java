package com.ked.interaction.commands;

import com.ked.core.services.TransactionService;
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
public class AddTransactionCommand extends BotCommand {
    private final ConversationService conversationService;
    private final TransactionService transactionService;

    public AddTransactionCommand(
            ConversationService conversationService,
            TransactionService transactionService
    ) {
        super("add_transaction", "Add transaction");
        this.conversationService = conversationService;
        this.transactionService = transactionService;
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
        conversationService.startConversation(chatId, EConversation.ADD_TRANSACTION, absSender);
    }
}
//    TODO : создать диалог
    /*
    выберите доход/расход
    выберите категорию
    впишите сумму (через пробле можно вписать комментарий)
     */
