package com.ked.tg.utils;

import java.util.regex.Pattern;

public class PhoneUtil {
    private static final Pattern PATTERN = Pattern.compile("^((8|\\+7)\\s?)?((\\(\\d{3}\\))|(\\d{3}))(\\s|-)?(\\d{3}-?\\d{2}-?\\d{2})$");
    private static final int PHONE_LENGTH_WITHOUT_CODE = 10;

    public static boolean isCorrectPhone(String phone) {
        return PATTERN.matcher(phone).matches();
    }

    public static String convertPhone(String phone) {
        if (!isCorrectPhone(phone)) {
            return null;
        }

        phone = phone
                .replaceAll("\\(", "")
                .replaceAll("\\)", "")
                .replaceAll("\\s", "")
                .replaceAll("-", "")
                .replaceAll("\\+7", "");

        if (phone.length() == PHONE_LENGTH_WITHOUT_CODE) {
            return "8".concat(phone);
        }

        return phone;
    }
}
