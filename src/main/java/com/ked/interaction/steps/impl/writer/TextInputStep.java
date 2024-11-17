package com.ked.interaction.steps.impl.writer;

import com.ked.interaction.steps.InputStep;
import com.ked.tg.dto.MessageDto;
import com.ked.tg.dto.ResultDto;
import com.ked.tg.entities.TgChat;
import com.ked.tg.enums.ERole;
import com.ked.tg.exceptions.EntityNotFoundBotException;
import com.ked.tg.services.BotMessageService;
import com.ked.tg.services.BotUserService;
import com.ked.tg.utils.MessageUtil;
import com.ked.tg.utils.ValidUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.bots.AbsSender;

@Component
@RequiredArgsConstructor
public class TextInputStep extends InputStep {
    private final BotMessageService botMessageService;

    private final BotUserService botUserService;

    private static final String PREPARE_MESSAGE_TEXT = "Введите текст, который необходимо отобразить в сообщении";

    private static final String ANSWER_MESSAGE_TEXT = "Текст добавлен";

    @Override
    public void prepare(TgChat tgChat, AbsSender sender) throws EntityNotFoundBotException {
        MessageUtil.sendMessage(tgChat.getChatId(), PREPARE_MESSAGE_TEXT, sender);
    }

    @Override
    protected int finishStep(TgChat tgChat, AbsSender sender, String data) throws EntityNotFoundBotException {
        saveMessageText(tgChat.getChatId(), data);
        MessageUtil.sendMessage(tgChat.getChatId(), ANSWER_MESSAGE_TEXT, sender);
        return 0;
    }

    @Override
    protected ResultDto isValidData(MessageDto messageDto) {
        if (ValidUtil.isLongBotMessage(messageDto.getData())) {
            String exceptionMessage = ValidUtil.getLongMessageExceptionText(ValidUtil.MAX_BOT_MESSAGE_LENGTH);
            return new ResultDto(false, exceptionMessage);
        }

        return new ResultDto(true);
    }

    private void saveMessageText(long chatId, String data) throws EntityNotFoundBotException {
        long userId = botUserService.getByChatIdAndRole(chatId, ERole.ROLE_WRITER).getId();
        botMessageService.saveText(userId, data);
    }
}
