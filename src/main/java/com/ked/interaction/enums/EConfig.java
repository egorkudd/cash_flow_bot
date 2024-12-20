package com.ked.interaction.enums;

import lombok.Getter;

import java.util.Arrays;

@Getter
public enum EConfig {
    NAME("Имя"),
    CATEGORY("Категории"),
    WEB_REGISTER("Регистрация на платформе"),
    EXIT("Выйти");

    private final String value;

    EConfig(String value) {
        this.value = value;
    }

    public static boolean isExists(String type) {
        return Arrays.stream(EConfig.values())
                .anyMatch(eConfig -> eConfig.toString().equals(type));
    }
}
