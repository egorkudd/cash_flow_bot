package com.ked.tg.exceptions;

public class CommandBotException extends AbstractBotException {
    public CommandBotException(String message, String userMessage) {
        super(message, userMessage);
    }
}
