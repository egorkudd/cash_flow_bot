package com.ked.interaction.enums;

import com.ked.core.enums.ETransaction;
import lombok.Getter;

import java.util.Arrays;

@Getter
public enum EConfig {
    NAME("Имя"),
    CATEGORY("Категории");

    private final String value;

    EConfig(String value) {
        this.value = value;
    }

    public static boolean isExists(String type) {
        return Arrays.stream(EConfig.values())
                .anyMatch(eConfig -> eConfig.toString().equals(type));
    }
}
