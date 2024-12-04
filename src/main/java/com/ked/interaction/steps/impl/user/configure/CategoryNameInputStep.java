package com.ked.interaction.steps.impl.user.configure;

import com.ked.core.entities.User;
import com.ked.core.services.CategoryService;
import com.ked.core.services.UserService;
import com.ked.interaction.steps.InputStep;
import com.ked.tg.dto.MessageDto;
import com.ked.tg.dto.ResultDto;
import com.ked.tg.entities.TgChat;
import com.ked.tg.exceptions.AbstractBotException;
import com.ked.tg.utils.MessageUtil;
import com.ked.tg.utils.StepUtil;
import com.ked.tg.utils.ValidUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.bots.AbsSender;

@Component
@RequiredArgsConstructor
public class CategoryNameInputStep extends InputStep {
    private static final String PREPARE_MESSAGE_TEXT = "Введите название категории";

    private static final String EXCEPTION_MESSAGE_TEXT = "Слишком длинное название, уместите его в 15 символов. Введите другое";

    private final CategoryService categoryService;

    private final UserService userService;

    @Override
    public void prepare(TgChat tgChat, AbsSender sender) throws AbstractBotException {
        StepUtil.sendPrepareMessageOnlyText(tgChat, PREPARE_MESSAGE_TEXT, sender);
    }

    @Override
    protected int finishStep(TgChat tgChat, AbsSender sender, String data) throws AbstractBotException {
        User user = userService.findByTgId(tgChat.getChatId());
        categoryService.setName(data, user.getId());
        categoryService.setCreatedAt(user.getId());
        MessageUtil.sendMessage(tgChat.getChatId(), "Успешно!", sender);
        return 0;
    }

    @Override
    protected ResultDto isValidData(MessageDto messageDto) {
        if (ValidUtil.isLongButtonText(messageDto.getData().length())) {
            return new ResultDto(false, EXCEPTION_MESSAGE_TEXT);
        }

        return new ResultDto(true);
    }
}
