package ru.javawebinar.topjava.util;

import org.springframework.core.convert.converter.Converter;

import java.time.LocalDate;

public class StringToLocalDate implements Converter<String, LocalDate> {

    public LocalDate convert(String source) {
        if (source.equals("")) {
            return null;
        }
        return LocalDate.parse(source);
    }
}
