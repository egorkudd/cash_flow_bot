package com.ked.tg.exceptions;

public class FileNotDownloadedBotException extends AbstractBotException {
    public FileNotDownloadedBotException(String message) {
        super(message, "Не удалось скачать файл, обратитесь в поддержку");
    }
}
