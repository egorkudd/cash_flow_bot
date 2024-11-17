package com.ked.tg.mappers;

import com.ked.tg.dto.MessageDto;
import com.ked.tg.entities.TgChat;
import com.ked.tg.enums.EMessage;
import com.ked.tg.utils.UpdateUtil;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public class MessageMapper {
    public MessageDto messageDto(Update update, EMessage eMessage, TgChat tgChat) {
        MessageDto messageDto = new MessageDto();
        messageDto.setChatId(tgChat.getChatId());
        messageDto.setEMessage(eMessage);
        messageDto.setData(UpdateUtil.getUserInputText(update));

        switch (eMessage) {
            case DOCUMENT -> messageDto.setDocument(update.getMessage().getDocument());
            case PHOTO -> messageDto.setPhotoList(update.getMessage().getPhoto());
        }

        return messageDto;
    }
}
