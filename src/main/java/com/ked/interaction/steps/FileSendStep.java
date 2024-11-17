package com.ked.interaction.steps;

import com.ked.tg.dto.MessageDto;
import com.ked.tg.dto.ResultDto;
import com.ked.tg.entities.TgChat;
import com.ked.tg.exceptions.AbstractBotException;
import com.ked.tg.exceptions.FileNotDownloadedBotException;
import org.telegram.telegrambots.meta.bots.AbsSender;

import java.io.File;

public abstract class FileSendStep extends ConversationStep {
    protected abstract ResultDto isValidFile(MessageDto messageDto, AbsSender sender);

    protected abstract File download(MessageDto messageDto, AbsSender sender);

    protected abstract void saveDocument(long chatId, String path);

    protected abstract String getAnswerMessageText();

    @Override
    public int execute(TgChat tgChat, MessageDto messageDto, AbsSender sender) throws AbstractBotException {
        ResultDto result = isValidFile(messageDto, sender);
        if (!result.isDone()) {
            return handleIllegalUserAction(messageDto, sender, result.getMessage());
        }

        downloadFile(tgChat.getChatId(), messageDto, sender);
        return finishStep(tgChat, sender, getAnswerMessageText());
    }

    protected void downloadFile(
            long chatId, MessageDto messageDto, AbsSender sender
    ) throws FileNotDownloadedBotException {
        File file = download(messageDto, sender);
        if (file != null) {
            saveDocument(chatId, file.getPath());
        } else {
            throw new FileNotDownloadedBotException("ChatId=%d не удалось скачать".formatted(chatId));
        }
    }
}
