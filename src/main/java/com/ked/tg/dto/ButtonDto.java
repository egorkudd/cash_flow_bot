package com.ked.tg.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class ButtonDto {
    private String callback;
    private String text;
    private String url;

    public ButtonDto(String callback, String text) {
        this.callback = callback;
        this.text = text;
    }

    public ButtonDto(String callback, String text, String url) {
        this.callback = callback;
        this.text = text;
        this.url = url;
    }

    public InlineKeyboardButton toKeyboardButton() {
        InlineKeyboardButton button = new InlineKeyboardButton();
        button.setCallbackData(callback);
        if (text != null) button.setText(text);
        if (url != null) button.setUrl(url);
        return button;
    }
}
