package com.ked.core.enums;

import lombok.Getter;

import java.util.Arrays;

@Getter
public enum ETimeInterval {
    TODAY("Сегодня"),
    WEEK("7 дней"),
    MONTH("Этот месяц"),
    YEAR("Этот год"),
    CUSTOM("Свои даты"),
    EXIT("Выйти");

    private final String value;

    ETimeInterval(String value) {
        this.value = value;
    }

    public static boolean isExists(String type) {
        return Arrays.stream(ETimeInterval.values())
                .anyMatch(eConfigCategory -> eConfigCategory.toString().equals(type));
    }
}
