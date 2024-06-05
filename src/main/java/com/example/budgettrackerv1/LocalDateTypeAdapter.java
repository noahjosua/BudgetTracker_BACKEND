package com.example.budgettrackerv1;

import com.google.gson.*;

import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class LocalDateTypeAdapter implements JsonSerializer<LocalDate>, JsonDeserializer<LocalDate> {

    private final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @Override
    public JsonElement serialize(final LocalDate date, final Type typeOfSrc,
                                 final JsonSerializationContext context) {
        return new JsonPrimitive(date.format(FORMATTER));
    }

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