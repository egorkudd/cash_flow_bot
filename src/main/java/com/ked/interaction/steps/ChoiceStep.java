package com.ked.interaction.steps;

import com.ked.tg.dto.MessageDto;
import com.ked.tg.dto.ResultDto;
import com.ked.tg.entities.TgChat;
import com.ked.tg.exceptions.EntityNotFoundBotException;
import com.ked.tg.utils.KeyboardUtil;
import org.telegram.telegrambots.meta.bots.AbsSender;

public abstract class ChoiceStep extends ConversationStep {
    protected static final String EXCEPTION_MESSAGE_TEXT = "Выберите один из выше предложенных вариантов";

    protected abstract ResultDto isValidData(MessageDto messageDto) throws EntityNotFoundBotException;

    @Override
    public int execute(TgChat tgChat, MessageDto messageDto, AbsSender sender) throws EntityNotFoundBotException {
        ResultDto result = isValidData(messageDto);
        if (!result.isDone()) {
            return handleIllegalUserAction(messageDto, sender, result.getMessage());
        }

        deleteKeyboard(tgChat, sender);
        return finishStep(tgChat, sender, messageDto.getData());
    }

    protected void deleteKeyboard(TgChat tgChat, AbsSender sender) {
        long chatId = tgChat.getChatId();
        int keyBoardMessageId = tgChat.getPrevBotMessageId();
        KeyboardUtil.cleanKeyboard(chatId, keyBoardMessageId, sender);
        tgChat.setDefaultPrevBotMessagePageNumber();
    }
}
