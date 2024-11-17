package com.ked.interaction.steps;

import com.ked.tg.dto.MessageDto;
import com.ked.tg.dto.ResultDto;
import com.ked.tg.entities.TgChat;
import com.ked.tg.exceptions.EntityNotFoundBotException;
import org.telegram.telegrambots.meta.bots.AbsSender;

public abstract class InputStep extends ConversationStep {
    protected abstract ResultDto isValidData(MessageDto messageDto);

    @Override
    public int execute(TgChat tgChat, MessageDto messageDto, AbsSender sender) throws EntityNotFoundBotException {
        ResultDto result = isValidData(messageDto);
        if (!result.isDone()) {
            return handleIllegalUserAction(messageDto, sender, result.getMessage());
        }

        return finishStep(tgChat, sender, messageDto.getData());
    }
}
