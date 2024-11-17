package com.ked.interaction.steps.impl.user.add_transaction;

import com.ked.core.entities.User;
import com.ked.core.services.TransactionService;
import com.ked.core.services.UserService;
import com.ked.interaction.enums.EConversationStep;
import com.ked.interaction.steps.InputStep;
import com.ked.tg.dto.MessageDto;
import com.ked.tg.dto.ResultDto;
import com.ked.tg.entities.TgChat;
import com.ked.tg.exceptions.AbstractBotException;
import com.ked.tg.utils.StepUtil;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.bots.AbsSender;

import java.math.BigDecimal;

@Component
@RequiredArgsConstructor
public class TransactionInputStep extends InputStep {
    @Getter
    private final EConversationStep name = EConversationStep.TRANSACTION_INPUT;
    private static final String PREPARE_MESSAGE_TEXT = "Введите сумму транзакции";
    private static final String EXCEPTION_MESSAGE_TEXT = "Неверный формат ввода. Пример: 50, 50.1, 50.10";

    private final TransactionService transactionService;

    private final UserService userService;

    @Override
    public void prepare(TgChat tgChat, AbsSender sender) throws AbstractBotException {
        StepUtil.sendPrepareMessageOnlyText(tgChat, PREPARE_MESSAGE_TEXT, sender);
    }

    @Override
    protected int finishStep(TgChat tgChat, AbsSender sender, String data) throws AbstractBotException {
        User user = userService.findByTgId(tgChat.getChatId());
        transactionService.setAmount(data, user.getId());
        transactionService.setCreatedAt(user.getId());
        return 0;
    }

    @Override
    protected ResultDto isValidData(MessageDto messageDto) {
        try {
            BigDecimal money = new BigDecimal(messageDto.getData());
            return new ResultDto(true);
        } catch (NumberFormatException e) {
            return new ResultDto(false, EXCEPTION_MESSAGE_TEXT);
        }
    }
}
