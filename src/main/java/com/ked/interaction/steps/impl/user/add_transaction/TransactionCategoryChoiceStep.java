package com.ked.interaction.steps.impl.user.add_transaction;

import com.ked.core.entities.Category;
import com.ked.core.entities.User;
import com.ked.core.services.CategoryService;
import com.ked.core.services.TransactionService;
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
public class TransactionCategoryChoiceStep extends ChoiceStep {
    private static final String PREPARE_MESSAGE_TEXT = "Выберите категорию транзакции";

    private final TransactionService transactionService;

    private final CategoryService categoryService;

    private final UserService userService;

    private final KeyboardMapper keyboardMapper;


    @Override
    protected ResultDto isValidData(Update update) throws EntityNotFoundBotException {
        if (!UpdateUtil.isCallback(update) ||
                !categoryService.exists(Long.valueOf(UpdateUtil.getUserInputText(update)))
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
        User user = userService.findByTgId(tgChat.getChatId());
        transactionService.setCategory(UpdateUtil.getUserInputText(update), user.getId());
        return 0;
    }

    private KeyboardDto getKeyboardDto(TgChat tgChat) {
        User user = userService.findByTgId(tgChat.getChatId());
        List<Category> categoryList = categoryService.findAllByUserIdToAddTransaction(user.getId());
        List<ButtonDto> buttonDtoList = new ArrayList<>();
        for (Category category : categoryList) {
            buttonDtoList.add(new ButtonDto(category.getId().toString(), category.getName()));
        }

        return keyboardMapper.keyboardDto(tgChat, buttonDtoList);
    }
}
