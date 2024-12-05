package com.ked.interaction.steps;

import com.ked.tg.dto.ResultDto;
import com.ked.tg.entities.TgChat;
import com.ked.tg.exceptions.EntityNotFoundBotException;
import com.ked.tg.utils.KeyboardUtil;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.bots.AbsSender;

public abstract class ChoiceStep extends ConversationStep {
    protected static final String EXCEPTION_MESSAGE_TEXT = "Выберите один из выше предложенных вариантов (кнопок под сообщением выше)";

    protected abstract ResultDto isValidData(Update update) throws EntityNotFoundBotException;

    @Override
    public int execute(TgChat tgChat, Update update, AbsSender sender) throws EntityNotFoundBotException {
        ResultDto result = isValidData(update);
        if (!result.isDone()) {
            return handleIllegalUserAction(update, sender, result.getMessage());
        }

        int stepNumber = finishStep(tgChat, sender, update);
        if (stepNumber != -1) {
            deleteKeyboard(tgChat, sender);
        }

        return stepNumber;
    }

    protected void deleteKeyboard(TgChat tgChat, AbsSender sender) {
        long chatId = tgChat.getChatId();
        int keyBoardMessageId = tgChat.getPrevBotMessageId();
        KeyboardUtil.cleanKeyboard(chatId, keyBoardMessageId, sender);
        tgChat.setDefaultPrevBotMessagePageNumber();
    }
}
