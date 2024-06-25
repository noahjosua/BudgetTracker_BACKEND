package com.team7.budgettracker.adapter;

import com.team7.budgettracker.Constants;
import com.google.gson.*;

import java.lang.reflect.Type;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * A Gson TypeAdapter for serializing and deserializing {@link LocalDate} objects.
 * This adapter converts {@link LocalDate} to and from its JSON representation using the
 * format "yyyy-MM-dd".
 * <p>
 * Example usage with Gson:
 * <pre>
 * Gson gson = new GsonBuilder().registerTypeAdapter(LocalDate.class, new LocalDateTypeAdapter()).create();
 * </pre>
 */
public class LocalDateTypeAdapter implements JsonSerializer<LocalDate>, JsonDeserializer<LocalDate> {

    private final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern(Constants.DATE_FORMAT);

    /**
     * Serializes the specified {@link LocalDate} into its JSON representation.
     *
     * @param date the {@link LocalDate} to be serialized
     * @param typeOfSrc the actual type of the source object
     * @param context the context of the serialization process
     * @return a {@link JsonElement} representing the specified {@link LocalDate}
     * @throws DateTimeException if an error occurs during formatting
     */
    @Override
    public JsonElement serialize(final LocalDate date, final Type typeOfSrc,
                                 final JsonSerializationContext context) throws DateTimeException {
        return new JsonPrimitive(date.format(FORMATTER));
    }

    /**
     * Deserializes the specified JSON element into a {@link LocalDate}.
     * If the JSON element contains a time component (indicated by the presence of 'T'),
     * it will be ignored.
     *
     * @param json the JSON element to be deserialized
     * @param typeOfT the type of the object to be deserialized
     * @param context the context of the deserialization process
     * @return the deserialized {@link LocalDate}
     * @throws JsonParseException if the JSON element cannot be parsed into a {@link LocalDate}
     */
    @Override
    public LocalDate deserialize(final JsonElement json, final Type typeOfT,
                                 final JsonDeserializationContext context) throws JsonParseException {
        String dateString = json.getAsString();
        if (dateString.contains("T")) {
            dateString = dateString.split("T")[0]; // Extract the date portion
        }
        return LocalDate.parse(dateString, FORMATTER);
    }
}