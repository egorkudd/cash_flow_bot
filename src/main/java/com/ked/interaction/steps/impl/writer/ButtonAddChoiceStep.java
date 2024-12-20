package com.ked.interaction.steps.impl.writer;

import com.ked.interaction.steps.ChoiceStep;
import com.ked.tg.dto.ResultDto;
import com.ked.tg.entities.TgChat;
import com.ked.tg.enums.EYesNo;
import com.ked.tg.exceptions.EntityNotFoundBotException;
import com.ked.tg.mappers.KeyboardMapper;
import com.ked.tg.utils.ButtonUtil;
import com.ked.tg.utils.StepUtil;
import com.ked.tg.utils.UpdateUtil;
import com.ked.tg.utils.ValidUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.bots.AbsSender;

@Component
@RequiredArgsConstructor
public class ButtonAddChoiceStep extends ChoiceStep {
    private final KeyboardMapper keyboardMapper;

    private static final String PREPARE_MESSAGE_TEXT = "Вы хотите добавить кнопку-ссылку?";

    @Override
    protected ResultDto isValidData(Update update) throws EntityNotFoundBotException {
        return ValidUtil.isValidYesNoChoice(update, EXCEPTION_MESSAGE_TEXT);
    }

    @Override
    public void prepare(TgChat tgChat, Update update, AbsSender sender) throws EntityNotFoundBotException {
        StepUtil.sendPrepareMessageWithInlineKeyBoard(
                tgChat,
                PREPARE_MESSAGE_TEXT,
                keyboardMapper.keyboardDto(tgChat, ButtonUtil.yesNoButtonList()),
                sender
        );
    }

    @Override
    protected int finishStep(TgChat tgChat, AbsSender sender, Update update) throws EntityNotFoundBotException {
        if (EYesNo.YES.toString().equals(UpdateUtil.getUserInputText(update))) {
            return 0;
        }

        return 1;
    }
}
