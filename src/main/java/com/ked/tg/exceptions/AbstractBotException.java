package com.ked.tg.exceptions;

import lombok.Getter;

@Getter
public abstract class AbstractBotException extends RuntimeException {
    protected String userMessage;

    public AbstractBotException(String message, String userMessage) {
        super(message);
        this.userMessage = userMessage;
    }
}
