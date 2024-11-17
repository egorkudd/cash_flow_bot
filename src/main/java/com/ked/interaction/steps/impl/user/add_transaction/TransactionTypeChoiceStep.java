package com.ked.interaction.steps.impl.user.add_transaction;

import com.ked.core.entities.User;
import com.ked.core.enums.ETransaction;
import com.ked.core.services.TransactionService;
import com.ked.core.services.UserService;
import com.ked.interaction.enums.EConversationStep;
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
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.bots.AbsSender;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class TransactionTypeChoiceStep extends ChoiceStep {
    @Getter
    private final EConversationStep name = EConversationStep.TRANSACTION_TYPE_CHOICE;

    private static final String PREPARE_MESSAGE_TEXT = "Выберите тип транзакции";

    private final KeyboardMapper keyboardMapper;

    private final TransactionService transactionService;

    private final UserService userService;

    @Override
    protected ResultDto isValidData(MessageDto messageDto) throws EntityNotFoundBotException {
        if (!ValidUtil.isCallback(messageDto.getEMessage()) || !ETransaction.isExists(messageDto.getData())) {
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
        User user = userService.findByTgId(tgChat.getChatId());
        transactionService.setType(data, user.getId());
        return 0;
    }

    private KeyboardDto getKeyboardDto(TgChat tgChat) {
        ETransaction[] eTransactionArray = ETransaction.values();
        List<ButtonDto> buttonDtoList = new ArrayList<>();
        for (ETransaction eTransaction : eTransactionArray) {
            buttonDtoList.add(new ButtonDto(eTransaction.toString(), eTransaction.getValue()));
        }

        return keyboardMapper.keyboardDto(tgChat, buttonDtoList);
    }
}
