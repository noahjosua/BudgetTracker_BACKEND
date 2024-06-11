package com.example.budgettrackerv1.helper;

import com.example.budgettrackerv1.Constants;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.Optional;

public class GetEntriesByDateHelper {

    private static final Logger LOGGER = LogManager.getLogger(GetEntriesByDateHelper.class);

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
            LOGGER.error("Could not get calendar from date {}.", date, e);
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
            LOGGER.error("Could not get calendar from date {}.", date, e);
            return Optional.empty();
        }
    }

    private static Optional<LocalDate> getDate(Map<String, LocalDate> map, String key) {
        try {
            if (map.containsKey(key)) {
                return Optional.of(map.get(key));
            }
        } catch (ClassCastException | NullPointerException e) {
            LOGGER.error("Could not get calendar from date for key {}.", key, e);
        }
        return Optional.empty();
    }
}
