package com.ked.interaction.steps.impl.user.statistic;

import com.ked.core.entities.User;
import com.ked.core.services.StatisticService;
import com.ked.core.services.UserService;
import com.ked.interaction.steps.ChoiceStep;
import com.ked.tg.dto.ResultDto;
import com.ked.tg.entities.TgChat;
import com.ked.tg.exceptions.AbstractBotException;
import com.ked.tg.utils.DateUtil;
import com.ked.tg.utils.MessageUtil;
import com.ked.tg.utils.StepUtil;
import com.ked.tg.utils.UpdateUtil;
import io.github.dostonhamrakulov.InlineCalendarBuilder;
import io.github.dostonhamrakulov.InlineCalendarCommandUtil;
import io.github.dostonhamrakulov.LanguageEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.bots.AbsSender;

import java.time.LocalDate;

@Slf4j
@Component
@RequiredArgsConstructor
public class StatisticStartDateChoiceStep extends ChoiceStep {
    private static final String PREPARE_MESSAGE_TEXT = "Введите начальную дату";

    private static final InlineCalendarBuilder inlineCalendarBuilder = new InlineCalendarBuilder(LanguageEnum.RU);

    private final StatisticService statisticService;

    private final UserService userService;

    @Override
    public void prepare(TgChat tgChat, Update update, AbsSender sender) throws AbstractBotException {
        inlineCalendarBuilder.setShowFullMonthName(true);
        StepUtil.sendPrepareMessageWithInlineKeyBoard(
                tgChat, PREPARE_MESSAGE_TEXT, inlineCalendarBuilder.build(update), sender
        );
    }

    @Override
    protected int finishStep(TgChat tgChat, AbsSender sender, Update update) throws AbstractBotException {
        if (InlineCalendarCommandUtil.isInlineCalendarClicked(update)){
            if (InlineCalendarCommandUtil.isCalendarIgnoreButtonClicked(update)) {
                return -1;
            }

            if (InlineCalendarCommandUtil.isCalendarNavigationButtonClicked(update)) {
                EditMessageReplyMarkup edit = MessageUtil.completeEditMessageReplyMarkup(
                        UpdateUtil.getChatId(update),
                        tgChat.getPrevBotMessageId(),
                        inlineCalendarBuilder.build(update)
                );
                MessageUtil.editMessageReplyMarkup(edit, sender);
                return -1;
            }

            String dateStr = DateUtil.convertDate(InlineCalendarCommandUtil.extractDate(update));
            User user = userService.findByTgId(UpdateUtil.getChatId(update));
            statisticService.setStartDate(dateStr, user.getId());
            MessageUtil.sendMessage(UpdateUtil.getChatId(update), "Начало: ".concat(dateStr), sender);
            return 0;
        }

        return -1;
    }

    @Override
    protected ResultDto isValidData(Update update) {
        return new ResultDto(true);
    }
}
