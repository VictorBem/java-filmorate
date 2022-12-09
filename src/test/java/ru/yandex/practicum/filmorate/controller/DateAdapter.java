package ru.yandex.practicum.filmorate.controller;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

//Адаптер для преобразования LocalDate в Json и обратно из Json в LocalDate - используется только в тестах
public class DateAdapter extends TypeAdapter<LocalDate> {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE;

    @Override
    public void write(JsonWriter jsonWriter, LocalDate date) throws IOException {
        if (date == null) {
            jsonWriter.value("");
        } else {
            jsonWriter.value(date.format(formatter));
        }
    }

    @Override
    public LocalDate read(JsonReader jsonReader) throws IOException {
        String date = jsonReader.nextString();
        if (!date.equals("")) {
            return LocalDate.parse(date, formatter);
        } else {
            return null;
        }
    }

}
