package com.ked.interaction.steps.impl.writer;

import com.ked.interaction.steps.ChoiceStep;
import com.ked.tg.dto.ResultDto;
import com.ked.tg.entities.TgChat;
import com.ked.tg.enums.EChat;
import com.ked.tg.enums.ERole;
import com.ked.tg.mappers.KeyboardMapper;
import com.ked.tg.services.BotMessageService;
import com.ked.tg.services.BotUserService;
import com.ked.tg.utils.ButtonUtil;
import com.ked.tg.utils.StepUtil;
import com.ked.tg.utils.UpdateUtil;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.bots.AbsSender;

@Component
@RequiredArgsConstructor
public class ChatTypeChoiceStep extends ChoiceStep {
    private final KeyboardMapper keyboardMapper;

    private final BotMessageService botMessageService;

    private final BotUserService botUserService;

    private static final String PREPARE_MESSAGE_TEXT = "Вы хотите разослать это ссобщение только участникамм конкретного мероприятия?";

    @Override
    protected ResultDto isValidData(Update update) throws EntityNotFoundException {
        if (!UpdateUtil.isCallback(update)) {
            return new ResultDto(false, EXCEPTION_MESSAGE_TEXT);
        }

        try {
            EChat.valueOf(UpdateUtil.getUserInputText(update));
            return new ResultDto(true);
        } catch (IllegalArgumentException ignored) {
        }

        return new ResultDto(false, EXCEPTION_MESSAGE_TEXT);
    }

    @Override
    public void prepare(TgChat chatHash, Update update, AbsSender sender) throws EntityNotFoundException {
        StepUtil.sendPrepareMessageWithInlineKeyBoard(
                chatHash,
                PREPARE_MESSAGE_TEXT,
                keyboardMapper.keyboardDto(chatHash, ButtonUtil.chatTypeButtonList()),
                sender
        );
    }

    @Override
    protected int finishStep(TgChat chatHash, AbsSender sender, Update update) throws EntityNotFoundException {
        EChat eChat = EChat.valueOf(UpdateUtil.getUserInputText(update));
        long userId = botUserService.getByChatIdAndRole(chatHash.getChatId(), ERole.ROLE_WRITER).getId();
        botMessageService.saveChatType(userId, eChat);
        return 0;
    }
}
