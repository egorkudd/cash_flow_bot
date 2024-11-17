package com.ked.tg.builders;

import com.ked.tg.dto.ButtonDto;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class InlineKeyboardMarkupBuilder {
    protected final List<List<InlineKeyboardButton>> rowButtonList = new ArrayList<>();

    public static InlineKeyboardMarkupBuilder create() {
        return new InlineKeyboardMarkupBuilder();
    }

    public InlineKeyboardMarkup build() {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        inlineKeyboardMarkup.setKeyboard(rowButtonList);
        return inlineKeyboardMarkup;
    }

    public InlineKeyboardMarkupBuilder setButtonList(List<ButtonDto> buttonDtoList) {
        for (int i = 0; i < buttonDtoList.size(); i++) {
            addButton(buttonDtoList.get(i), i);
        }

        return this;
    }

    protected void addButton(ButtonDto buttonDto, int row) {
        while (rowButtonList.size() <= row) {
            rowButtonList.add(new ArrayList<>());
        }

        rowButtonList.get(row).add(buttonDto.toKeyboardButton());
    }
}
