package com.ked.interaction.steps.impl.user.configure;

import com.ked.interaction.enums.EConfig;
import com.ked.interaction.steps.ChoiceStep;
import com.ked.tg.dto.ButtonDto;
import com.ked.tg.dto.KeyboardDto;
import com.ked.tg.dto.ResultDto;
import com.ked.tg.entities.TgChat;
import com.ked.tg.exceptions.AbstractBotException;
import com.ked.tg.exceptions.EntityNotFoundBotException;
import com.ked.tg.mappers.KeyboardMapper;
import com.ked.tg.utils.StepUtil;
import com.ked.tg.utils.UpdateUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.bots.AbsSender;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ConfigureChoiceStep extends ChoiceStep {
    private static final String PREPARE_MESSAGE_TEXT = "Что вы хотите изменить";

    private final KeyboardMapper keyboardMapper;

    @Override
    protected ResultDto isValidData(Update update) throws EntityNotFoundBotException {
        if (!UpdateUtil.isCallback(update) || !EConfig.isExists(UpdateUtil.getUserInputText(update))
        ) {
            return new ResultDto(false, EXCEPTION_MESSAGE_TEXT);
        }

        return new ResultDto(true);
    }

    @Override
    public void prepare(TgChat tgChat, Update update, AbsSender sender) throws AbstractBotException {
        StepUtil.sendPrepareMessageWithInlineKeyBoard(
                tgChat, PREPARE_MESSAGE_TEXT, getKeyboardDto(tgChat), sender
        );
    }

    @Override
    protected int finishStep(TgChat tgChat, AbsSender sender, Update update) throws AbstractBotException {
        EConfig eConfig = EConfig.valueOf(UpdateUtil.getUserInputText(update));
        return switch (eConfig) {
            case NAME -> 0;
            case CATEGORY -> 1;
            case WEB_REGISTER -> 2;
            default -> 3;
        };
    }

    private KeyboardDto getKeyboardDto(TgChat tgChat) {
        EConfig[] eConfigArray = EConfig.values();
        List<ButtonDto> buttonDtoList = new ArrayList<>();
        for (EConfig eConfig : eConfigArray) {
            buttonDtoList.add(new ButtonDto(eConfig.toString(), eConfig.getValue()));
        }

        return keyboardMapper.keyboardDto(tgChat, buttonDtoList);
    }
}
