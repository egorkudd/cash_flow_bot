package com.ked.interaction.enums;

import lombok.Getter;

import java.util.Arrays;

@Getter
public enum EConfigCategory {
    ADD("Добавить"),
    RENAME("Переименовать"),
    EXIT("Выйти");

    private final String value;

    EConfigCategory(String value) {
        this.value = value;
    }

    public static boolean isExists(String type) {
        return Arrays.stream(EConfigCategory.values())
                .anyMatch(eConfigCategory -> eConfigCategory.toString().equals(type));
    }
}
