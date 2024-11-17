package com.ked.tg.utils;

import com.ked.tg.dto.ResultDto;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class DateUtil {
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd.MM.yyyy");
    private static final int MAX_AGE = 122;

    public static ResultDto isValidBirthday(String birthdayStr) {
        Date date = DateUtil.convertDate(birthdayStr);
        if (date == null) {
            return new ResultDto(false, "Некорректный формат даты. Введите данные по шаблону <b>дд.мм.гггг</b>");
        }

        if (!isRealBirthday(date)) {
            return new ResultDto(false, "Вы ввели невозможную дату рождения. Введите свои настоящие данные");
        }

        return new ResultDto(true);
    }

    public static boolean isRealBirthday(Date birthday) {
        int yearCount = DateUtil.getYearCountByDate(birthday);
        return new Date().after(birthday) && yearCount < MAX_AGE;
    }

    public static Date convertDate(String birthdayStr) {
        try {
            return DATE_FORMAT.parse(birthdayStr);
        } catch (ParseException ignored) {
        }

        return null;
    }

    public static int getYearCountByDate(Date date) {
        TimeZone timeZone = TimeZone.getTimeZone("Europe/Moscow");
        Calendar calDate = Calendar.getInstance(timeZone);
        calDate.setTime(date);
        Calendar calToday = Calendar.getInstance(timeZone);
        calToday.setTime(new Date());

        int yearsDifference = calToday.get(Calendar.YEAR) - calDate.get(Calendar.YEAR);
        if (!wasBirthdayThisYear(calDate, calToday)) {
            yearsDifference--;
        }

        return yearsDifference;
    }

    private static boolean wasBirthdayThisYear(Calendar birthday, Calendar today) {
        if (today.get(Calendar.MONTH) < birthday.get(Calendar.MONTH)) {
            return false;
        }

        if (today.get(Calendar.MONTH) > birthday.get(Calendar.MONTH)) {
            return true;
        }

        return today.get(Calendar.DAY_OF_MONTH) >= birthday.get(Calendar.DAY_OF_MONTH);
    }
}
