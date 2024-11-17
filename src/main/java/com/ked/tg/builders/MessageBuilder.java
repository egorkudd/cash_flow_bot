package com.ked.tg.builders;

import com.ked.tg.dto.KeyboardDto;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

import java.io.File;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MessageBuilder {
    private String text;
    private InlineKeyboardMarkup keyboardMarkup;
    private InputFile inputFile;

    public static MessageBuilder create() {
        return new MessageBuilder();
    }

    public MessageBuilder setText(String text) {
        this.text = text;
        return this;
    }

    public MessageBuilder setInlineKeyBoard(KeyboardDto keyboardDto) {
        this.keyboardMarkup = InlineKeyboardMarkupBuilder.create()
                .setButtonList(keyboardDto.getButtonDtoList())
                .build();
        return this;
    }

    public MessageBuilder setPageableKeyBoard(KeyboardDto keyboardDto) {
        this.keyboardMarkup = PageableInlineKeyboardMarkupBuilder.create()
                .setPageNumber(keyboardDto.getPageNumber())
                .setButtonList(keyboardDto.getButtonDtoList())
                .build();
        return this;
    }

    public MessageBuilder setFile(File file) {
        this.inputFile = new InputFile(file);
        return this;
    }

    public SendMessage sendMessage(long chatId) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        if (text != null) sendMessage.setText(text);
        if (keyboardMarkup != null) sendMessage.setReplyMarkup(keyboardMarkup);
        return sendMessage;
    }

    public SendDocument sendDocument(long chatId) {
        SendDocument sendDocument = new SendDocument();
        sendDocument.setChatId(chatId);
        if (inputFile != null) sendDocument.setDocument(inputFile);
        if (text != null) sendDocument.setCaption(text);
        if (keyboardMarkup != null) sendDocument.setReplyMarkup(keyboardMarkup);
        return sendDocument;
    }
}
