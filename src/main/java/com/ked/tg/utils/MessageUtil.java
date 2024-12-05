package com.ked.tg.utils;

import com.ked.tg.builders.MessageBuilder;
import com.ked.tg.builders.PageableInlineKeyboardMarkupBuilder;
import com.ked.tg.dto.KeyboardDto;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.bots.DefaultAbsSender;
import org.telegram.telegrambots.meta.api.methods.GetFile;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup;
import org.telegram.telegrambots.meta.api.objects.File;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Slf4j
public class MessageUtil {
    private static final String EXCEPTION_MESSAGE_TEMPLATE = "Для ID чата {} не удалось отправить сообщение. Причина: {}";
    private static final String EXCEPTION_MESSAGE_DOWNLOAD_FILE = "Не удалось скачать файл. Причина: {}";
    private static final String FILE_STORAGE_PATH = "src/main/resources/static/";

    public static Message sendMessage(long chatId, String text, AbsSender sender) {
        return MessageUtil.sendMessage(
                MessageBuilder.create().setText(text).sendMessage(chatId),
                sender
        );
    }

    public static void editMessageReplyMarkup(KeyboardDto keyboardDto, AbsSender sender) {
        EditMessageReplyMarkup editMessageReplyMarkup = editMessageWithKeyboard(keyboardDto);
        editMessageReplyMarkup(editMessageReplyMarkup, sender);
    }

    public static java.io.File downloadFile(String fileId, AbsSender sender) {
        GetFile getFile = completeGetFile(fileId);
        File fileInfo = getFile(getFile, sender);
        if (fileInfo != null) {
            return downloadFile(fileInfo, sender);
        }

        return null;
    }

    public static Message sendFile(long chatId, java.io.File file, String text, AbsSender sender) {
        return MessageUtil.sendDocument(
                MessageBuilder.create().setFile(file).setText(text).sendDocument(chatId),
                sender
        );
    }

    public static Message sendPhoto(Long chatId, java.io.File file, AbsSender sender) {
        return sendPhoto(new SendPhoto(chatId.toString(), new InputFile(file)), sender);
    }


    public static Message sendMessage(SendMessage sendMessage, AbsSender sender) {
        try {
            sendMessage.enableHtml(true);
            sendMessage.disableWebPagePreview();
            return sender.execute(sendMessage);
        } catch (TelegramApiException e) {
            log.error(EXCEPTION_MESSAGE_TEMPLATE, sendMessage.getChatId(), e.getMessage());
        }

        return null;
    }

    public static Message sendDocument(SendDocument sendDocument, AbsSender sender) {
        try {
            return sender.execute(sendDocument);
        } catch (TelegramApiException e) {
            log.error(EXCEPTION_MESSAGE_TEMPLATE, sendDocument.getChatId(), e.getMessage());
        }

        return null;
    }

    public static Message sendPhoto(SendPhoto sendPhoto, AbsSender sender) {
        try {
            return sender.execute(sendPhoto);
        } catch (TelegramApiException e) {
            log.error(EXCEPTION_MESSAGE_TEMPLATE, sendPhoto.getChatId(), e.getMessage());
        }

        return null;
    }

    private static EditMessageReplyMarkup editMessageWithKeyboard(KeyboardDto keyboardDto) {
        InlineKeyboardMarkup inlineKeyboardMarkup = inlineKeyboard(keyboardDto);
        return MessageUtil.completeEditMessageReplyMarkup(
                keyboardDto.getChatId(), keyboardDto.getMessageId(), inlineKeyboardMarkup
        );
    }

    private static InlineKeyboardMarkup inlineKeyboard(KeyboardDto keyboardDto) {
        return PageableInlineKeyboardMarkupBuilder.create()
                .setPageNumber(keyboardDto.getPageNumber())
                .setButtonList(keyboardDto.getButtonDtoList())
                .build();
    }

    public static EditMessageReplyMarkup completeEditMessageReplyMarkup(
            long chatId, Integer messageId, InlineKeyboardMarkup inlineKeyboardMarkup
    ) {
        EditMessageReplyMarkup edit = new EditMessageReplyMarkup();
        edit.setMessageId(messageId);
        edit.setReplyMarkup(inlineKeyboardMarkup);
        edit.setChatId(chatId);
        return edit;
    }

    private static GetFile completeGetFile(String fileId) {
        GetFile getFile = new GetFile();
        getFile.setFileId(fileId);
        return getFile;
    }

    public static void editMessageReplyMarkup(EditMessageReplyMarkup edit, AbsSender sender) {
        try {
            sender.execute(edit);
        } catch (TelegramApiException e) {
            log.error(EXCEPTION_MESSAGE_TEMPLATE, edit.getChatId(), e.getMessage());
            e.printStackTrace();
        }
    }

    private static File getFile(GetFile getFile, AbsSender sender) {
        try {
            return sender.execute(getFile);
        } catch (TelegramApiException e) {
            log.error(EXCEPTION_MESSAGE_DOWNLOAD_FILE, e.getMessage());
        }

        return null;
    }

    private static java.io.File downloadFile(File fileInfo, AbsSender sender) {
        try {
            String filePath = fileInfo.getFilePath();
            return ((DefaultAbsSender) sender).downloadFile(
                    filePath, new java.io.File(FILE_STORAGE_PATH.concat(filePath))
            );
        } catch (TelegramApiException e) {
            log.error(EXCEPTION_MESSAGE_DOWNLOAD_FILE, e.getMessage());
        }

        return null;
    }
}
