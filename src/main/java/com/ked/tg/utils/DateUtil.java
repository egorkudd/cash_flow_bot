package com.ked.tg.utils;

import com.ked.tg.dto.ResultDto;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class DateUtil {
    private static final String DATE_FORMAT_STR = "dd.MM.yyyy";
    private static final SimpleDateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat(DATE_FORMAT_STR);
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern(DATE_FORMAT_STR);
    private static final int MAX_AGE = 122;

    public static boolean isValidDate(String dateStr) {
        return convertDate(dateStr) != null;
    }

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

    public static Date convertDate(String dateStr) {
        try {
            return SIMPLE_DATE_FORMAT.parse(dateStr);
        } catch (ParseException ignored) {
        }

        return null;
    }

    public static Instant convertInstant(String dateStr) {
        try {
            return SIMPLE_DATE_FORMAT.parse(dateStr).toInstant();
        } catch (ParseException ignored) {
        }

        return null;
    }

    public static String convertDate(Date date) {
        return SIMPLE_DATE_FORMAT.format(date);
    }

    public static String convertDate(LocalDate date) {
        return date.format(DATE_TIME_FORMATTER);
    }

    public static String convertDate(LocalDateTime date) {
        return SIMPLE_DATE_FORMAT.format(date);
    }

    public static String convertDate(Instant instant) {
        return SIMPLE_DATE_FORMAT.format(Date.from(instant));
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
