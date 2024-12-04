package com.ked.interaction.steps.impl.user.configure;

import com.ked.core.entities.Category;
import com.ked.core.entities.User;
import com.ked.core.services.CategoryService;
import com.ked.core.services.UserService;
import com.ked.interaction.steps.ChoiceStep;
import com.ked.tg.dto.ButtonDto;
import com.ked.tg.dto.KeyboardDto;
import com.ked.tg.dto.MessageDto;
import com.ked.tg.dto.ResultDto;
import com.ked.tg.entities.TgChat;
import com.ked.tg.exceptions.AbstractBotException;
import com.ked.tg.exceptions.EntityNotFoundBotException;
import com.ked.tg.mappers.KeyboardMapper;
import com.ked.tg.utils.StepUtil;
import com.ked.tg.utils.ValidUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.bots.AbsSender;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class CategoryChoiceStep extends ChoiceStep {
    private static final String PREPARE_MESSAGE_TEXT = "Выберите категорию транзакции";

    private final CategoryService categoryService;

    private final UserService userService;

    private final KeyboardMapper keyboardMapper;


    @Override
    protected ResultDto isValidData(MessageDto messageDto) throws EntityNotFoundBotException {
        if (!ValidUtil.isCallback(messageDto.getEMessage()) ||
                !categoryService.exists(Long.valueOf(messageDto.getData()))
        ) {
            return new ResultDto(false, EXCEPTION_MESSAGE_TEXT);
        }

        return new ResultDto(true);
    }

    @Override
    public void prepare(TgChat tgChat, AbsSender sender) throws AbstractBotException {
        StepUtil.sendPrepareMessageWithInlineKeyBoard(
                tgChat, PREPARE_MESSAGE_TEXT, getKeyboardDto(tgChat), sender
        );
    }

    @Override
    protected int finishStep(TgChat tgChat, AbsSender sender, String data) throws AbstractBotException {
        categoryService.deleteCreatedAt(data);
        return 0;
    }

    private KeyboardDto getKeyboardDto(TgChat tgChat) {
        User user = userService.findByTgId(tgChat.getChatId());
        List<Category> categoryList = categoryService.findAllByUserIdToChoose(user.getId());
        List<ButtonDto> buttonDtoList = new ArrayList<>();
        for (Category category : categoryList) {
            buttonDtoList.add(new ButtonDto(category.getId().toString(), category.getName()));
        }

        return keyboardMapper.keyboardDto(tgChat, buttonDtoList);
    } // TODO: вынести куда-то
}
