package com.team7.budgettracker.helper;

import com.team7.budgettracker.Constants;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.Optional;

/**
 * Helper class for handling operations related to retrieving entries by date.
 * Provides methods for generating success and error messages, as well as utility
 * functions for date and calendar manipulation.
 */
public class GetEntriesByDateHelper {

    private static final Logger LOGGER = LogManager.getLogger(GetEntriesByDateHelper.class);

    /**
     * Generates a success message for a request based on the provided date and type.
     *
     * @param date the date of the request
     * @param type the type of the request
     * @return a success message string
     */
    public static String getSuccessMessageForByDateRequest(Date date, String type) {
        Optional<Calendar> calendar = GetEntriesByDateHelper.getCalendar(date);
        return calendar.map(value -> String.format("%s for month %s were retrieved from database.", type, value.get(Calendar.MONTH) + 1)).orElse(String.format("%s were retrieved from database.", type));
    }

    /**
     * Generates an error message for a request based on the provided date and type.
     *
     * @param date the date of the request
     * @param type the type of the request
     * @return an error message string
     */
    public static String getErrorMessageForByDateRequest(Date date, String type) {
        Optional<Calendar> calendar = GetEntriesByDateHelper.getCalendar(date);
        return calendar.map(value -> String.format("%s for month %s could not be retrieved from database.", type, value.get(Calendar.MONTH) + 1)).orElse(String.format("%s could not be retrieved from database.", type));
    }

    /**
     * Retrieves a {@link LocalDate} based on the provided date and key.
     *
     * @param date the date of the request
     * @param key the key to retrieve the specific date
     * @return an {@link Optional} containing the {@link LocalDate}, if present
     */
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

    /**
     * Converts a {@link Date} to a {@link Calendar}.
     *
     * @param date the date to be converted
     * @return an {@link Optional} containing the {@link Calendar}, if conversion is successful
     */
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

    /**
     * Generates a map containing the start and end dates of the month for the given date.
     *
     * @param date the date for which to get the start and end dates of the month
     * @return an {@link Optional} containing the map of start and end dates
     */
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

    /**
     * Retrieves a {@link LocalDate} from the map based on the provided key.
     *
     * @param map the map containing date information
     * @param key the key to retrieve the specific date
     * @return an {@link Optional} containing the {@link LocalDate}, if present
     */
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
