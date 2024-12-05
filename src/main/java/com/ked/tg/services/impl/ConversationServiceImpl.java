package com.ked.tg.services.impl;

import com.ked.interaction.enums.EConversation;
import com.ked.interaction.enums.EConversationStep;
import com.ked.tg.entities.TgChat;
import com.ked.tg.enums.EMessage;
import com.ked.tg.exceptions.AbstractBotException;
import com.ked.tg.exceptions.CommandBotException;
import com.ked.tg.services.ConversationService;
import com.ked.tg.services.ConversationStepService;
import com.ked.tg.services.TgChatService;
import com.ked.tg.utils.LogUtil;
import com.ked.tg.utils.MessageUtil;
import com.ked.tg.utils.UpdateUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.bots.AbsSender;

@Slf4j
@Service
@RequiredArgsConstructor
public class ConversationServiceImpl implements ConversationService {
    private final ConversationStepService conversationStepService;
    private final TgChatService tgChatService;

    @Override
    public void startConversation(Update update, EConversation eConversation, AbsSender sender) throws AbstractBotException {
        TgChat tgChat = createChatHash(UpdateUtil.getChatId(update), eConversation);
        conversationStepService.prepareStep(new Update(), tgChat, sender);
        tgChatService.save(tgChat);
    }

    @Override
    public void executeConversationStep(
            Update update, EMessage eMessage, AbsSender sender
    ) throws AbstractBotException {
        long chatId = UpdateUtil.getChatId(update);
        TgChat tgChat = tgChatService.getChatById(chatId);
        log.info(LogUtil.getConversationLog(update, eMessage, tgChat));

        if (tgChat != null) {
            executeConversationStep(tgChat, update, sender);
        }
    }

    private void executeConversationStep(TgChat tgChat, Update update, AbsSender sender) throws AbstractBotException {
        handleCommand(update);

        EConversationStep prevStep = tgChat.getEConversationStep();
        EConversationStep nextStep = conversationStepService.executeStep(tgChat, update, sender);

        if (nextStep == null) {
            handleConversationEnd(tgChat, sender);
            return;
        }

        if (isStepCompleted(nextStep, prevStep)) {
            tgChat.setEConversationStep(nextStep);
            conversationStepService.prepareStep(update, tgChat, sender);
        }

        tgChatService.save(tgChat);
    }

    private boolean isStepCompleted(EConversationStep nextStep, EConversationStep prevStep) {
        return !prevStep.equals(nextStep);
    }

    private TgChat createChatHash(long chatId, EConversation eConversation) {
        EConversationStep startStep = conversationStepService.getStartStep(eConversation);
        return tgChatService.createChatHash(chatId, eConversation, startStep);
    }

    private void handleConversationEnd(TgChat tgChat, AbsSender sender) {
        String finishMessageText = conversationStepService.getFinishMessageText(
                tgChat.getEConversation()
        );
        if (finishMessageText != null) {
            MessageUtil.sendMessage(tgChat.getChatId(), finishMessageText, sender);
        }

        tgChatService.delete(tgChat);
    }

    private void handleCommand(Update update) throws CommandBotException {
        if (UpdateUtil.isCommand(update)) {
            throw new CommandBotException(
                    "Попытка вызвать команду во время действующего диалога",
                    "Вы не можете ввести другую команду, пока не завершите данный диалог"
            );
        }
    }
}
