package com.ked.interaction.steps;

import com.ked.tg.builders.MessageBuilder;
import com.ked.tg.entities.TgChat;
import com.ked.tg.exceptions.AbstractBotException;
import com.ked.tg.utils.MessageUtil;
import com.ked.tg.utils.UpdateUtil;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.bots.AbsSender;

public abstract class ConversationStep {
    public abstract void prepare(TgChat tgChat, Update update, AbsSender sender) throws AbstractBotException;

    public abstract int execute(TgChat tgChat, Update update, AbsSender sender) throws AbstractBotException;

    protected abstract int finishStep(TgChat tgChat, AbsSender sender, Update update) throws AbstractBotException;

    protected void sendFinishMessage(TgChat tgChat, AbsSender sender, String text) {
        SendMessage sendMessage = MessageBuilder.create()
                .setText(text)
                .sendMessage(tgChat.getChatId());
        MessageUtil.sendMessage(sendMessage, sender);
    }

    protected int handleIllegalUserAction(
            Update update, AbsSender sender, String exceptionMessageText
    ) {
        SendMessage sendMessage = MessageBuilder.create()
                .setText(exceptionMessageText)
                .sendMessage(UpdateUtil.getChatId(update));
        MessageUtil.sendMessage(sendMessage, sender);
        return -1;
    }
}
