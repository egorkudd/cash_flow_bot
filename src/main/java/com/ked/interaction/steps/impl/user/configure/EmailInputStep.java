package com.ked.interaction.steps.impl.user.configure;

import com.ked.core.services.UserService;
import com.ked.interaction.steps.InputStep;
import com.ked.tg.dto.MessageDto;
import com.ked.tg.dto.ResultDto;
import com.ked.tg.entities.TgChat;
import com.ked.tg.exceptions.AbstractBotException;
import com.ked.tg.utils.StepUtil;
import lombok.RequiredArgsConstructor;
import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.bots.AbsSender;

@Component
@RequiredArgsConstructor
public class EmailInputStep extends InputStep {
    private static final String PREPARE_MESSAGE_TEXT = "Введите email";

    private static final String VALID_EXCEPTION_MESSAGE_TEXT = "Email должен быть в виде user@example.com";

    private static final String EXISTS_EXCEPTION_MESSAGE_TEXT = "Такой email уже зарегистрирован";

    private static final EmailValidator emailValidator = EmailValidator.getInstance();

    private final UserService userService;


    @Override
    public void prepare(TgChat tgChat, AbsSender sender) throws AbstractBotException {
        StepUtil.sendPrepareMessageOnlyText(tgChat, PREPARE_MESSAGE_TEXT, sender);
    }

    @Override
    protected int finishStep(TgChat tgChat, AbsSender sender, String data) throws AbstractBotException {
        userService.setEmail(data, tgChat.getChatId());
        return 0;
    }

    @Override
    protected ResultDto isValidData(MessageDto messageDto) {
        String email = messageDto.getData();
        if (!emailValidator.isValid(email)) {
            return new ResultDto(false, VALID_EXCEPTION_MESSAGE_TEXT);
        }

        if (userService.existsByEmail(email)) {
            return new ResultDto(false, EXISTS_EXCEPTION_MESSAGE_TEXT);
        }

        return new ResultDto(true);
    }
}
