package com.ked.tg.services.impl;

import com.ked.tg.enums.EMessage;
import com.ked.tg.exceptions.AbstractBotException;
import com.ked.tg.exceptions.CommandBotException;
import com.ked.tg.services.ConversationService;
import com.ked.tg.services.GroupChatService;
import com.ked.tg.services.UpdateHandleService;
import com.ked.tg.utils.LogUtil;
import com.ked.tg.utils.MessageUtil;
import com.ked.tg.utils.UpdateUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.CommandRegistry;
import org.telegram.telegrambots.meta.api.objects.ChatMemberUpdated;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.bots.AbsSender;

@Slf4j
@Service
@RequiredArgsConstructor
public class UpdateHandleServiceImpl implements UpdateHandleService {
    private static final String EXCEPTION_MESSAGE = "Такой команды не существует";
    private final CommandRegistry commandRegistry;
    private final ConversationService conversationService;
    private final GroupChatService groupChatService;

    @Override
    public void onUpdateReceived(Update update, AbsSender sender) {
        try {
            if (update.hasCallbackQuery() && UpdateUtil.isPrivateChat(update)) {
                handleCallbackRequest(update, sender);
            } else if (update.hasMessage() && UpdateUtil.isPrivateChat(update)) {
                handleMessageRequest(update, sender);
            } else if (update.hasMyChatMember()) {
                handleMyChatMember(update);
            }
        } catch (AbstractBotException e) {
            log.error(LogUtil.getExceptionLog(update, e.getMessage()), e);
            MessageUtil.sendMessage(UpdateUtil.getChatId(update), e.getUserMessage(), sender);
        } catch (Exception e) {
            log.error(LogUtil.getExceptionLog(update, e.getMessage()), e);
            MessageUtil.sendMessage(UpdateUtil.getChatId(update), "Что-то пошло не так. Нужно обратиться в поддержку", sender);
        }
    }

    @Override
    public void handleMyChatMember(Update update) {
        ChatMemberUpdated chatMemberUpdated = update.getMyChatMember();
        if (groupChatService.isBotNewChatMember(chatMemberUpdated)) {
            groupChatService.save(chatMemberUpdated);
        } else if (groupChatService.isBotKickedChatMember(chatMemberUpdated)) {
            groupChatService.delete(chatMemberUpdated);
        }
    }

    @Override
    public void handleCallbackRequest(Update update, AbsSender sender) throws AbstractBotException {
        conversationService.executeConversationStep(update, EMessage.CALLBACK, sender);
    }

    @Override
    public void handleMessageRequest(Update update, AbsSender sender) throws AbstractBotException {
        Message message = update.getMessage();
        if (message.isCommand()) {
            executeCommand(update, sender);
        } else if (message.hasDocument()) {
            conversationService.executeConversationStep(update, EMessage.DOCUMENT, sender);
        } else if (message.hasPhoto()) {
            conversationService.executeConversationStep(update, EMessage.PHOTO, sender);
        } else if (message.hasText()) {
            conversationService.executeConversationStep(update, EMessage.TEXT, sender);
        }
    }

    private void executeCommand(Update update, AbsSender sender) throws AbstractBotException {
        Message message = update.getMessage();
        conversationService.executeConversationStep(update, EMessage.COMMAND, sender);
        if (!commandRegistry.executeCommand(sender, message)) {
            throw new CommandBotException(EXCEPTION_MESSAGE, EXCEPTION_MESSAGE);
        }
    }
}
