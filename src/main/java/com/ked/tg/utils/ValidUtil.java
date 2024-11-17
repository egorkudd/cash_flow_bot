package com.ked.tg.utils;

import com.ked.tg.dto.MessageDto;
import com.ked.tg.dto.ResultDto;
import com.ked.tg.enums.EMessage;
import com.ked.tg.enums.EYesNo;
import org.telegram.telegrambots.meta.api.objects.Document;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.regex.Pattern;

public class ValidUtil {
    public static final int MAX_DESCRIPTION_TEXT_LENGTH = 1023;
    public static final int MAX_BOT_MESSAGE_LENGTH = 2047;
    public static final int MAX_BUTTON_TEXT_LENGTH = 20;
    private static final String EXCEPTION_MESSAGE_TEXT = "Слишком длинное сообщение. Постарайтесь уложиться в %d символов";
    private static final int MAX_REGISTER_PLACE_LENGTH = 511;
    private static final Pattern digitPattern = Pattern.compile("^[0-9]+$");

    public static boolean isValidURL(String urlString) {
        try {
            new URL(urlString);
            return true;
        } catch (MalformedURLException e) {
            return false;
        }
    }

    public static boolean isLongButtonText(int length) {
        return length >= MAX_BUTTON_TEXT_LENGTH;
    }

    public static String getLongMessageExceptionText(int length) {
        length = length / 100 * 100;
        return EXCEPTION_MESSAGE_TEXT.formatted(length);
    }

    public static boolean isLongBotMessage(String text) {
        return text.length() > MAX_BOT_MESSAGE_LENGTH;
    }

    public static boolean containsDigitsOnly(String data) {
        return digitPattern.matcher(data).matches();
    }

    public static boolean isLongRegisterPlace(String registerPlace) {
        return registerPlace.length() > MAX_REGISTER_PLACE_LENGTH;
    }

    public static ResultDto isValidFile(MessageDto messageDto, long maxDocSizeKb) {
        Document document = messageDto.getDocument();

        if (document == null) {
            return new ResultDto(false, "Вам необходимо отправить документ в ответном сообщении");
        }

        if (document.getFileSize() > maxDocSizeKb * 1024) {
            return new ResultDto(false, "Размер документа не должен превышать ".concat(String.valueOf(maxDocSizeKb)).concat("KB"));
        }

        if (!document.getFileName().endsWith(".doc") && !document.getFileName().endsWith(".pdf")) {
            return new ResultDto(false, "Формат документа должен быть в формате .doc или .pdf");
        }

        return new ResultDto(true);
    }

    public static ResultDto isValidShortString(String string) {
        if (string.isBlank()) {
            return new ResultDto(false, "Вы ввели пустую строку. Попробуйте снова");
        }

        if (string.length() > MAX_DESCRIPTION_TEXT_LENGTH) {
            String exceptionMessage = ValidUtil.getLongMessageExceptionText(ValidUtil.MAX_DESCRIPTION_TEXT_LENGTH);
            return new ResultDto(false, exceptionMessage);
        }

        return new ResultDto(true);
    }

    public static ResultDto isValidYesNoChoice(MessageDto messageDto, String exceptionMessage) {
        if (!isCallback(messageDto.getEMessage())) {
            return new ResultDto(false, exceptionMessage);
        }

        try {
            EYesNo.valueOf(messageDto.getData());
            return new ResultDto(true);
        } catch (IllegalArgumentException ignored) {
        }

        return new ResultDto(false, exceptionMessage);
    }

    public static boolean isCallback(EMessage eMessage) {
        return EMessage.CALLBACK.equals(eMessage);
    }
}
