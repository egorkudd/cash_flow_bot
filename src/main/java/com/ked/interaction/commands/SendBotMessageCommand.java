package com.ked.interaction.commands;

import com.ked.interaction.enums.EConversation;
import com.ked.tg.entities.BotUser;
import com.ked.tg.enums.ERole;
import com.ked.tg.exceptions.EntityNotFoundBotException;
import com.ked.tg.services.BotMessageService;
import com.ked.tg.services.BotUserService;
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
public class SendBotMessageCommand extends BotCommand {
    private final ConversationService conversationService;
    private final BotUserService botUserService;
    private final BotMessageService botMessageService;

    public SendBotMessageCommand(
            ConversationService conversationService,
            BotUserService botUserService,
            BotMessageService botMessageService
    ) {
        super("send_message", "Send bot message to users");
        this.conversationService = conversationService;
        this.botUserService = botUserService;
        this.botMessageService = botMessageService;
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] arguments) {
        try {
            BotUser botUser = botUserService.getByChatIdAndRole(chat.getId(), ERole.ROLE_WRITER);
            botMessageService.create(botUser.getId());
            conversationService.startConversation(
                    chat.getId(), EConversation.SEND_BOT_MESSAGE, absSender
            );
        } catch (EntityNotFoundBotException e) {
            log.error(e.getMessage());
            MessageUtil.sendMessage(chat.getId(), "Недостаточно прав", absSender);
        }
    }
}
