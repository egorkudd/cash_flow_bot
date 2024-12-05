package com.ked.interaction.steps.impl.writer;

import com.ked.interaction.steps.InputStep;
import com.ked.tg.dto.ResultDto;
import com.ked.tg.entities.TgChat;
import com.ked.tg.enums.ERole;
import com.ked.tg.exceptions.EntityNotFoundBotException;
import com.ked.tg.services.BotMessageService;
import com.ked.tg.services.BotUserService;
import com.ked.tg.utils.MessageUtil;
import com.ked.tg.utils.UpdateUtil;
import com.ked.tg.utils.ValidUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.bots.AbsSender;

@Component
@RequiredArgsConstructor
public class ButtonInputStep extends InputStep {
    private final BotMessageService botMessageService;

    private final BotUserService botUserService;

    private static final String PREPARE_MESSAGE_TEXT = """
            1) Введите текст для кнопки
            2) Введите перенос на другую строку
            3) Введите ссылку (пожалуйста, скопируйте полную ссылку из браузера, она должна начинаться с http или https)
                        
            Пример:
            "google
            https://google.com"
            """;

    private static final String ANSWER_MESSAGE_TEXT = "Кнопка добавлена";

    @Override
    public void prepare(TgChat tgChat, Update update, AbsSender sender) throws EntityNotFoundBotException {
        MessageUtil.sendMessage(tgChat.getChatId(), PREPARE_MESSAGE_TEXT, sender);
    }

    @Override
    protected int finishStep(TgChat tgChat, AbsSender sender, Update update) throws EntityNotFoundBotException {
        createButton(tgChat.getChatId(), UpdateUtil.getUserInputText(update));
        MessageUtil.sendMessage(tgChat.getChatId(), ANSWER_MESSAGE_TEXT, sender);
        return 0;
    }

    @Override
    protected ResultDto isValidData(Update update) {
        String[] dataPartArray = UpdateUtil.getUserInputText(update).split("\n");

        if (dataPartArray.length != 2) {
            return new ResultDto(false, "Вы неверно ввели данные\n".concat(PREPARE_MESSAGE_TEXT));
        }

        if (ValidUtil.isLongButtonText(dataPartArray[0].length())) {
            return new ResultDto(false, ValidUtil.getLongMessageExceptionText(ValidUtil.MAX_BOT_MESSAGE_LENGTH));
        }

        if (!ValidUtil.isValidURL(dataPartArray[1])) {
            return new ResultDto(false, "Некорректная ссылка\n".concat(PREPARE_MESSAGE_TEXT));
        }

        return new ResultDto(true);
    }

    private void createButton(long chatId, String data) throws EntityNotFoundBotException {
        long userId = botUserService.getByChatIdAndRole(chatId, ERole.ROLE_WRITER).getId();

        String[] dataPartArray = data.split("\n");
        String buttonName = dataPartArray[0];
        String buttonLink = dataPartArray[1];

        botMessageService.createButton(userId, buttonName, buttonLink);
    }
}
