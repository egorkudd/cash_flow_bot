package com.ked.interaction.steps.impl.user.configure;

import com.ked.core.services.UserService;
import com.ked.interaction.steps.InputStep;
import com.ked.tg.dto.MessageDto;
import com.ked.tg.dto.ResultDto;
import com.ked.tg.entities.TgChat;
import com.ked.tg.exceptions.AbstractBotException;
import com.ked.tg.utils.StepUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.bots.AbsSender;

@Component
@RequiredArgsConstructor
public class PasswordInputStep extends InputStep {
    private static final String PREPARE_MESSAGE_TEXT = "Введите пароль (после успешного сохранения вы можете удалить сообщение с паролем)";

    private static final String EXCEPTION_MESSAGE_TEXT = "Пароль должен быть от 6 до 255 символов";

    private final UserService userService;


    @Override
    public void prepare(TgChat tgChat, AbsSender sender) throws AbstractBotException {
        StepUtil.sendPrepareMessageOnlyText(tgChat, PREPARE_MESSAGE_TEXT, sender);
    }

    @Override
    protected int finishStep(TgChat tgChat, AbsSender sender, String data) throws AbstractBotException {
        userService.setPassword(data, tgChat.getChatId());
        return 0;
    }

    @Override
    protected ResultDto isValidData(MessageDto messageDto) {
        String password = messageDto.getData();
        if (6 > password.length() || password.length() > 255) {
            return new ResultDto(false, EXCEPTION_MESSAGE_TEXT);
        }

        return new ResultDto(true);
    }
}
