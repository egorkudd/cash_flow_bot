package com.ked.tg.dto;

import com.ked.tg.enums.EMessage;
import lombok.Getter;
import lombok.Setter;
import org.telegram.telegrambots.meta.api.objects.Document;
import org.telegram.telegrambots.meta.api.objects.PhotoSize;

import java.util.List;

@Getter
@Setter
public class MessageDto {
    private long chatId;
    private String data;
    private Document document;
    private List<PhotoSize> photoList;
    private EMessage eMessage;
}
