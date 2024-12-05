package com.ked.interaction.steps;

import com.ked.tg.dto.ResultDto;
import com.ked.tg.entities.TgChat;
import com.ked.tg.exceptions.EntityNotFoundBotException;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.bots.AbsSender;

public abstract class InputStep extends ConversationStep {
    protected abstract ResultDto isValidData(Update update);

    @Override
    public int execute(TgChat tgChat, Update update, AbsSender sender) throws EntityNotFoundBotException {
        ResultDto result = isValidData(update);
        if (!result.isDone()) {
            return handleIllegalUserAction(update, sender, result.getMessage());
        }

        return finishStep(tgChat, sender, update);
    }
}
