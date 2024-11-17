package com.ked.core.enums;

import lombok.Getter;

import java.util.Arrays;

@Getter
public enum ETimeInterval {
    DAY("День"),
    WEEK("Неделя"),
    MONTH("Месяц"),
    YEAR("Год"),
    CUSTOM("Ввести свои даты");

    private final String value;

    ETimeInterval(String value) {
        this.value = value;
    }

    public static boolean isExists(String type) {
        return Arrays.stream(ETimeInterval.values())
                .anyMatch(eConfigCategory -> eConfigCategory.toString().equals(type));
    }
}
