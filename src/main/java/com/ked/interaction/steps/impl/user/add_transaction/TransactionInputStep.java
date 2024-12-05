package com.ked.interaction.steps.impl.user.add_transaction;

import com.ked.core.entities.User;
import com.ked.core.services.TransactionService;
import com.ked.core.services.UserService;
import com.ked.interaction.steps.InputStep;
import com.ked.tg.dto.ResultDto;
import com.ked.tg.entities.TgChat;
import com.ked.tg.exceptions.AbstractBotException;
import com.ked.tg.utils.MessageUtil;
import com.ked.tg.utils.StepUtil;
import com.ked.tg.utils.UpdateUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.bots.AbsSender;

import java.math.BigDecimal;

@Component
@RequiredArgsConstructor
public class TransactionInputStep extends InputStep {
    private static final BigDecimal MAX_MONEY_VALUE = new BigDecimal(9_000_000_000_000_000_000L);

    private static final String PREPARE_MESSAGE_TEXT = "Введите сумму транзакции";

    private static final String EXCEPTION_MESSAGE_TEXT = """
            Неверный формат ввода. Введите другую сумму
            Пример: 50, 50.1, 50.10
            P.S. Сумма должна быть меньше %s
            """.formatted(MAX_MONEY_VALUE);

    private final TransactionService transactionService;

    private final UserService userService;

    @Override
    public void prepare(TgChat tgChat, Update update, AbsSender sender) throws AbstractBotException {
        StepUtil.sendPrepareMessageOnlyText(tgChat, PREPARE_MESSAGE_TEXT, sender);
    }

    @Override
    protected int finishStep(TgChat tgChat, AbsSender sender, Update update) throws AbstractBotException {
        User user = userService.findByTgId(tgChat.getChatId());
        transactionService.setAmount(UpdateUtil.getUserInputText(update), user.getId());
        transactionService.setCreatedAt(user.getId());
        MessageUtil.sendMessage(tgChat.getChatId(), "Транзакция успешно добавлена!", sender);
        return 0;
    }

    @Override
    protected ResultDto isValidData(Update update) {
        try {
            String moneyStr = UpdateUtil.getUserInputText(update);
            BigDecimal money = new BigDecimal(moneyStr);

            if (moneyStr.contains(".") && moneyStr.split("\\.")[1].length() > 2) {
                return new ResultDto(false, EXCEPTION_MESSAGE_TEXT);
            }

            if (MAX_MONEY_VALUE.compareTo(money) > 0) {
                return new ResultDto(true);
            }
        } catch (NumberFormatException ignored) {
        }

        return new ResultDto(false, EXCEPTION_MESSAGE_TEXT);
    }
}
