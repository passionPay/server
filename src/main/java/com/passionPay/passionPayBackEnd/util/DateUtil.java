package com.passionPay.passionPayBackEnd.util;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import static java.time.temporal.ChronoUnit.DAYS;

public class DateUtil {
    public static String formatDateToString(LocalDate date) {
        return date.format(DateTimeFormatter.ISO_DATE);
    }

    public static String formatTimeToString(LocalTime time) {
        return time.format(DateTimeFormatter.ISO_LOCAL_TIME);
    }

    public static LocalDate parseStringToDate(String date) {
        if (date == null) return null;
        return LocalDate.parse(date, DateTimeFormatter.ISO_DATE);
    }

    public static LocalTime parseStringToTime(String time) {
        return LocalTime.parse(time, DateTimeFormatter.ISO_LOCAL_TIME);
    }

    public static long formatDDayToInt(LocalDate dDay) {
        if (dDay == null) return -1;

        LocalDate today = LocalDate.now();
        return DAYS.between(today, dDay);
    }

    public static long getTimeBetween(LocalTime start, LocalTime end) {
        if (start == null || end == null || end.isBefore(start)) return -1;
        return Duration.between(start, end).toMillis();
    }
}
