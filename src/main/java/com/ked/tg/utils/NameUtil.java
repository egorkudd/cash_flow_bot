package com.ked.tg.utils;

import com.ked.tg.dto.ResultDto;

import java.util.regex.Pattern;

public class NameUtil {
    private static final Pattern letterPattern = Pattern.compile("^[а-яА-ЯёЁa-zA-Z]+$");
    private static final int SURNAME_INDEX = 0;
    private static final int NAME_INDEX = 1;
    private static final int PATRONYMIC_INDEX = 2;
    private static final int FULL_NAME_PART_NUMBER_WITHOUT_PATRONYMIC = 2;
    private static final int FULL_NAME_PART_NUMBER_WITH_PATRONYMIC = 3;
    private static final int MAX_FULL_NAME_LENGTH = 511;


    public static boolean isCorrectFullNameWordCount(String fullName) {
        String[] fullNamePartArray = fullName.split("\\s");
        return fullNamePartArray.length == FULL_NAME_PART_NUMBER_WITHOUT_PATRONYMIC ||
                fullNamePartArray.length == FULL_NAME_PART_NUMBER_WITH_PATRONYMIC;
    }

    public static boolean fullNameContainsIncorrectSymbols(String nameWord) {
        return !letterPattern.matcher(nameWord).matches();
    }

    public static ResultDto isValidFullName(String fullName) {
        if (isLongFullName(fullName)) {
            return new ResultDto(false, "Ваше ФИО слишком длинное, по нашим данным такого существовать не может");
        }

        if (!NameUtil.isCorrectFullNameWordCount(fullName)) {
            return new ResultDto(false, "Ваше ФИО должно содержать только фамилию из 1 слова, имя из одного слова и отчество из одного слова при наличии");
        }

        String[] fullNamePartArray = fullName.split("\\s+");
        if (NameUtil.fullNameContainsIncorrectSymbols(fullNamePartArray[SURNAME_INDEX])) {
            return new ResultDto(false, "Фамилия должна содержать только буквы");
        }

        if (NameUtil.fullNameContainsIncorrectSymbols(fullNamePartArray[NAME_INDEX])) {
            return new ResultDto(false, "Имя должно содержать только буквы");
        }

        if (fullNamePartArray.length == FULL_NAME_PART_NUMBER_WITH_PATRONYMIC
                && NameUtil.fullNameContainsIncorrectSymbols(fullNamePartArray[PATRONYMIC_INDEX])
        ) {
            return new ResultDto(false, "Отчество должно содержать только буквы");
        }

        return new ResultDto(true);
    }

    public static boolean isLongFullName(String fullName) {
        return fullName.length() > MAX_FULL_NAME_LENGTH;
    }

}