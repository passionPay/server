package com.passionPay.passionPayBackEnd.util;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static java.time.temporal.ChronoUnit.DAYS;

public class DateUtil {
    public static String formatDateToString(LocalDate date) {
        return date.format(DateTimeFormatter.ISO_DATE);
    }

    public static String formatTimeToString(LocalTime time) {
        return time.format(DateTimeFormatter.ISO_LOCAL_TIME);
    }

    public static String formatDateTimeToString(LocalDateTime dateTime) {
        if (dateTime == null) return null;
        return dateTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
    }

    public static LocalDate parseStringToDate(String date) {
        if (date == null) return null;
        return LocalDate.parse(date, DateTimeFormatter.ISO_DATE);
    }

    public static LocalTime parseStringToTime(String time) {
        return LocalTime.parse(time, DateTimeFormatter.ISO_LOCAL_TIME);
    }

    public static List<LocalDate> parseMonthString(String month) {
        String[] yearAndMonth = month.split("-");
        if (yearAndMonth.length != 2 || yearAndMonth[0].length() != 4 || yearAndMonth[1].length() != 2)
            return null;

        LocalDate monthStart = LocalDate.of(Integer.parseInt(yearAndMonth[0]), Integer.parseInt(yearAndMonth[1]), 1);
        LocalDate monthEnd = LocalDate.of(Integer.parseInt(yearAndMonth[0]), Integer.parseInt(yearAndMonth[1]), monthStart.lengthOfMonth()-1);

        List<LocalDate> localDates = new ArrayList<>();
        localDates.add(monthStart);
        localDates.add(monthEnd);

        return localDates;
    }

    public static long formatDDayToInt(LocalDate dDay) {
        if (dDay == null) return -1;

        LocalDate today = LocalDate.now();
        long dayCount = DAYS.between(today, dDay);
        return (dayCount > -1? dayCount : -1 );
    }

    public static long getTimeBetween(LocalTime start, LocalTime end) {
        if (start == null || end == null || end.isBefore(start)) return -1;
        return Duration.between(start, end).toMillis();
    }
}
