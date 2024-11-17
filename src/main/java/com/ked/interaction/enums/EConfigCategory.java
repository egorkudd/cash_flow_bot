package com.ked.interaction.enums;

import com.ked.core.enums.ETransaction;
import lombok.Getter;

import java.util.Arrays;

@Getter
public enum EConfigCategory {
    ADD("Добавить"),
    RENAME("Переименовать");

    private final String value;

    EConfigCategory(String value) {
        this.value = value;
    }

    public static boolean isExists(String type) {
        return Arrays.stream(EConfigCategory.values())
                .anyMatch(eConfigCategory -> eConfigCategory.toString().equals(type));
    }
}
