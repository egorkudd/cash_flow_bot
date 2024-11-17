package com.ked.interaction.steps;

import com.ked.interaction.enums.EConversationStep;
import com.ked.tg.builders.MessageBuilder;
import com.ked.tg.dto.MessageDto;
import com.ked.tg.entities.TgChat;
import com.ked.tg.exceptions.AbstractBotException;
import com.ked.tg.utils.MessageUtil;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.bots.AbsSender;

public abstract class ConversationStep {
    public abstract EConversationStep getName();

    public abstract void prepare(TgChat tgChat, AbsSender sender) throws AbstractBotException;

    public abstract int execute(TgChat tgChat, MessageDto messageDto, AbsSender sender) throws AbstractBotException;

    protected abstract int finishStep(TgChat tgChat, AbsSender sender, String data) throws AbstractBotException;

    protected void sendFinishMessage(TgChat tgChat, AbsSender sender, String text) {
        SendMessage sendMessage = MessageBuilder.create()
                .setText(text)
                .sendMessage(tgChat.getChatId());
        MessageUtil.sendMessage(sendMessage, sender);
    }

    protected int handleIllegalUserAction(
            MessageDto messageDto, AbsSender sender, String exceptionMessageText
    ) {
        SendMessage sendMessage = MessageBuilder.create()
                .setText(exceptionMessageText)
                .sendMessage(messageDto.getChatId());
        MessageUtil.sendMessage(sendMessage, sender);
        return -1;
    }
}
