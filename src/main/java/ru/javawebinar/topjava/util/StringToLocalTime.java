package ru.javawebinar.topjava.util;

import org.springframework.core.convert.converter.Converter;

import java.time.LocalTime;

public class StringToLocalTime implements Converter<String, LocalTime> {

    public LocalTime convert(String source) {
        if (source.equals("")) {
            return null;
        }
        return LocalTime.parse(source);
    }
}
