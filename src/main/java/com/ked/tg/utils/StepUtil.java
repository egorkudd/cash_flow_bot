package com.ked.tg.utils;

import com.ked.tg.builders.MessageBuilder;
import com.ked.tg.dto.KeyboardDto;
import com.ked.tg.dto.MessageDto;
import com.ked.tg.entities.TgChat;
import com.ked.tg.enums.EPageMove;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.bots.AbsSender;

public class StepUtil {
    public static void sendPrepareMessageOnlyText(TgChat tgChat, String messageText, AbsSender sender) {
        SendMessage sendMessage = MessageBuilder.create().setText(messageText)
                .sendMessage(tgChat.getChatId());

        Message message = MessageUtil.sendMessage(sendMessage, sender);
        int messageId = message != null ? message.getMessageId() : -1;
        tgChat.setPrevBotMessageId(messageId);
    }

    public static void sendPrepareMessageWithInlineKeyBoard(
            TgChat tgChat, String messageText, KeyboardDto keyboardDto, AbsSender sender
    ) {
        Message message = MessageUtil.sendMessage(
                MessageBuilder.create().setText(messageText).setInlineKeyBoard(keyboardDto)
                        .sendMessage(tgChat.getChatId()),
                sender
        );

        int messageId = message != null ? message.getMessageId() : -1;
        tgChat.setPrevBotMessageId(messageId);
    }

    public static void sendPrepareMessageWithPageableKeyBoard(
            TgChat tgChat, String messageText, KeyboardDto keyboardDto, AbsSender sender
    ) {
        Message message = MessageUtil.sendMessage(
                MessageBuilder.create().setText(messageText).setPageableKeyBoard(keyboardDto)
                        .sendMessage(tgChat.getChatId()),
                sender
        );

        int messageId = message != null ? message.getMessageId() : -1;
        tgChat.setPrevBotMessageId(messageId);
    }

    public static boolean isMovePageAction(TgChat tgChat, MessageDto messageDto, KeyboardDto keyboardDto, AbsSender sender) {
        try {
            EPageMove ePageMove = EPageMove.valueOf(messageDto.getData());
            changePage(ePageMove, keyboardDto, tgChat, sender);
            return true;
        } catch (IllegalArgumentException ignored) {
        }

        return false;
    }

    private static void changePage(EPageMove ePageMove, KeyboardDto keyboardDto, TgChat tgChat, AbsSender sender) {
        int newPageNumber = KeyboardUtil.movePage(ePageMove, keyboardDto, sender);
        tgChat.setPrevBotMessagePageNumber(newPageNumber);
    }
}
