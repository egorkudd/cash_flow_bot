package com.ked.tg.exceptions;

public class EntityNotFoundBotException extends AbstractBotException {
    public EntityNotFoundBotException(String message) {
        super(message, "Что-то пошло не так, пожалуйста обратитесь в поддержку");
    }

    public EntityNotFoundBotException(String message, String userMessage) {
        super(message, userMessage);
    }
}
