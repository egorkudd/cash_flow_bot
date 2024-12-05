package com.ked.interaction.steps.impl.user.statistic;

import com.ked.core.dto.StatisticInfo;
import com.ked.core.entities.Statistic;
import com.ked.core.entities.User;
import com.ked.core.enums.ETimeInterval;
import com.ked.core.services.StatisticService;
import com.ked.core.services.UserService;
import com.ked.interaction.steps.ChoiceStep;
import com.ked.tg.dto.ButtonDto;
import com.ked.tg.dto.KeyboardDto;
import com.ked.tg.dto.ResultDto;
import com.ked.tg.entities.TgChat;
import com.ked.tg.exceptions.AbstractBotException;
import com.ked.tg.exceptions.EntityNotFoundBotException;
import com.ked.tg.mappers.KeyboardMapper;
import com.ked.tg.utils.MessageUtil;
import com.ked.tg.utils.StepUtil;
import com.ked.tg.utils.UpdateUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.bots.AbsSender;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@Component
@RequiredArgsConstructor
public class StatisticPeriodChoiceStep extends ChoiceStep {
    private static final String PREPARE_MESSAGE_TEXT = "Выберите период";

    private final StatisticService statisticService;

    private final UserService userService;

    private final KeyboardMapper keyboardMapper;

    @Override
    protected ResultDto isValidData(Update update) throws EntityNotFoundBotException {
        if (!UpdateUtil.isCallback(update) ||
                !ETimeInterval.isExists(UpdateUtil.getUserInputText(update))
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
        String data = UpdateUtil.getUserInputText(update);
        if (!ETimeInterval.EXIT.toString().equals(data)) {
            User user = userService.findByTgId(tgChat.getChatId());
            statisticService.setPeriod(data, user.getId());

            Statistic statistic = statisticService.setCreatedAt(user.getId());
            StatisticInfo info = statisticService.getTransactionStatisticByTimeInterval(
                    user.getId(), statistic.getETimeInterval(), Calendar.getInstance().toInstant()
            );

            String messageText = statisticService.collectStatisticMessage(info, ETimeInterval.valueOf(data));
            MessageUtil.sendMessage(tgChat.getChatId(), messageText, sender);
        }

        return 0;
    }

    private KeyboardDto getKeyboardDto(TgChat tgChat) {
        ETimeInterval[] eTimeIntervalArray = ETimeInterval.values();
        List<ButtonDto> buttonDtoList = new ArrayList<>();
        for (ETimeInterval eTimeInterval : eTimeIntervalArray) {
            buttonDtoList.add(new ButtonDto(eTimeInterval.toString(), eTimeInterval.getValue()));
        }

        return keyboardMapper.keyboardDto(tgChat, buttonDtoList);
    }
}
