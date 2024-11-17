package com.ked.tg.services.impl;

import com.ked.interaction.conversations.AConversation;
import com.ked.interaction.enums.EConversation;
import com.ked.interaction.enums.EConversationStep;
import com.ked.interaction.steps.ConversationStep;
import com.ked.tg.dto.MessageDto;
import com.ked.tg.entities.TgChat;
import com.ked.tg.exceptions.AbstractBotException;
import com.ked.tg.services.ConversationStepService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.bots.AbsSender;

import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class ConversationStepServiceImpl implements ConversationStepService {
    private final Map<EConversation, AConversation> conversationMap;
    private final Map<EConversationStep, ConversationStep> conversationStepMap;

    @Override
    public EConversationStep getStartStep(EConversation eConversation) {
        return conversationMap.get(eConversation).getStartStep();
    }

    @Override
    public void prepareStep(TgChat tgChat, AbsSender sender) throws AbstractBotException {
        ConversationStep step = getConversationStep(tgChat);
        step.prepare(tgChat, sender);
    }

    @Override
    public EConversationStep executeStep(
            TgChat tgChat, MessageDto messageDto, AbsSender sender
    ) throws AbstractBotException {
        ConversationStep step = getConversationStep(tgChat);
        int stepIndex = step.execute(tgChat, messageDto, sender);
        if (stepIndex == -1) {
            return tgChat.getEConversationStep();
        }

        return getNextEStep(tgChat, stepIndex);
    }

    @Override
    public String getFinishMessageText(EConversation eConversation) {
        return conversationMap.get(eConversation).getFinishMessageText();
    }

    private ConversationStep getConversationStep(TgChat tgChat) {
        return conversationStepMap.get(tgChat.getEConversationStep());
    }

    private EConversationStep getNextEStep(TgChat tgChat, int stepIndex) {
        return conversationMap
                .get(tgChat.getEConversation())
                .getNextStep(tgChat.getEConversationStep(), stepIndex);

    }
}
