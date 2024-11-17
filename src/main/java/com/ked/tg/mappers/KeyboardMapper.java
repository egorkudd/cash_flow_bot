package com.ked.tg.mappers;

import com.ked.tg.dto.ButtonDto;
import com.ked.tg.dto.KeyboardDto;
import com.ked.tg.entities.TgChat;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class KeyboardMapper {
    public KeyboardDto keyboardDto(TgChat tgChat, List<ButtonDto> buttonDtoList) {
        KeyboardDto keyboardDto = new KeyboardDto();
        keyboardDto.setChatId(tgChat.getChatId());
        keyboardDto.setPageNumber(tgChat.getPrevBotMessagePageNumber());
        keyboardDto.setMessageId(tgChat.getPrevBotMessageId());
        keyboardDto.setButtonDtoList(buttonDtoList);
        return keyboardDto;
    }
}
