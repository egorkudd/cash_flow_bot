package com.ked.interaction.steps.impl.user.configure.category;

import com.ked.core.entities.Category;
import com.ked.core.entities.User;
import com.ked.core.enums.ETransaction;
import com.ked.core.services.CategoryService;
import com.ked.core.services.UserService;
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
public class CategoryChoiceStep extends ChoiceStep {
    private static final String PREPARE_MESSAGE_TEXT = "Выберите категорию транзакции";

    private final CategoryService categoryService;

    private final UserService userService;

    private final KeyboardMapper keyboardMapper;

    @Override
    protected ResultDto isValidData(Update update) throws EntityNotFoundBotException {
        String data = UpdateUtil.getUserInputText(update);
        if ("Выйти".equals(data)) {
            return new ResultDto(true);
        }

        try {
            if (!UpdateUtil.isCallback(update) ||
                    !categoryService.exists(Long.valueOf(data))
            ) {
                return new ResultDto(false, EXCEPTION_MESSAGE_TEXT);
            }
        } catch (NumberFormatException e) {
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
        String data = UpdateUtil.getUserInputText(update);
        if ("Выйти".equals(data)) {
            return 1;
        }

        categoryService.deleteCreatedAt(data);
        return 0;
    }

    private KeyboardDto getKeyboardDto(TgChat tgChat) {
        User user = userService.findByTgId(tgChat.getChatId());
        List<Category> categoryList = categoryService.findAllByUserIdToRename(user.getId());
        List<ButtonDto> buttonDtoList = new ArrayList<>();
        for (Category category : categoryList) {
            buttonDtoList.add(new ButtonDto(
                    category.getId().toString(),
                    "%s (%s)".formatted(
                            category.getName(),
                            ETransaction.INCOME.equals(category.getType()) ? "I" : "E"
                    )
            ));
        }

        buttonDtoList.add(new ButtonDto("Выйти", "Выйти"));

        return keyboardMapper.keyboardDto(tgChat, buttonDtoList);
    }
}
