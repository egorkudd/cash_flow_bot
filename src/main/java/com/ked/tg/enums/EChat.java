package com.ked.tg.enums;

import lombok.Getter;

@Getter
public enum EChat {
    PERSONAL("Личные чаты"),
    GROUP("Групповые чаты");

    private final String value;

    EChat(String value) {
        this.value = value;
    }
}
