package com.example.budgettrackerv1.helper;

import com.example.budgettrackerv1.Constants;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.Optional;

public class GetEntriesByDateHelper {

    public static String getSuccessMessageForByDateRequest(Date date, String type) {
        Optional<Calendar> calendar = GetEntriesByDateHelper.getCalendar(date);
        return calendar.map(value -> String.format("%s for month %s were retrieved from database.", type, value.get(Calendar.MONTH) + 1)).orElse(String.format("%s were retrieved from database.", type));
    }

    public static String getErrorMessageForByDateRequest(Date date, String type) {
        Optional<Calendar> calendar = GetEntriesByDateHelper.getCalendar(date);
        return calendar.map(value -> String.format("%s for month %s could not be retrieved from database.", type, value.get(Calendar.MONTH) + 1)).orElse(String.format("%s could not be retrieved from database.", type));
    }

    public static Optional<LocalDate> getDate(Date date, String key) {
        Optional<Map<String, LocalDate>> startEndDate = getStartAndEndDateAsMap(date);
        if (startEndDate.isPresent()) {
            Optional<LocalDate> d = getDate(startEndDate.get(), key);
            if (d.isPresent()) {
                return d;
            }
        }
        return Optional.empty();
    }

    public static Optional<Calendar> getCalendar(Date date) {
        Calendar calendar = Calendar.getInstance();
        try {
            calendar.setTime(date);
            return Optional.of(calendar);
        } catch (NullPointerException e) {
            System.out.println("Could not get calendar!");
        }
        return Optional.empty();
    }

    private static Optional<Map<String, LocalDate>> getStartAndEndDateAsMap(Date date) {
        Calendar calendar = Calendar.getInstance();
        try {
            calendar.setTime(date);
            LocalDate firstDay = LocalDate.of(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, 1);
            LocalDate lastDay = firstDay.withDayOfMonth(firstDay.lengthOfMonth());
            return Optional.of(Map.of(
                    Constants.FIRST_DAY_KEY, firstDay,
                    Constants.LAST_DAY_KEY, lastDay
            ));
        } catch (IllegalArgumentException | NullPointerException | DateTimeException e) {
            System.out.println("Could not get start and end Date!");
            return Optional.empty();
        }
    }

    private static Optional<LocalDate> getDate(Map<String, LocalDate> map, String key) {
        try {
            if (map.containsKey(key)) {
                return Optional.of(map.get(key));
            }
        } catch (ClassCastException | NullPointerException e) {
            System.out.println("Could not get date!");
        }
        return Optional.empty();
    }
}
