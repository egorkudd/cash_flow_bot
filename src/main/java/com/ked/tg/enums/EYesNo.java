package com.ked.tg.enums;

import lombok.Getter;

@Getter
public enum EYesNo {
    YES("ДА"),
    NO("НЕТ");

    private final String value;

    EYesNo(String value) {
        this.value = value;
    }
}
