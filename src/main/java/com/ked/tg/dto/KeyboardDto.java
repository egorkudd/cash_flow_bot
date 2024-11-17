package com.ked.tg.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class KeyboardDto {
    private long chatId;
    private int messageId;
    private int pageNumber;
    private List<ButtonDto> buttonDtoList;
}
