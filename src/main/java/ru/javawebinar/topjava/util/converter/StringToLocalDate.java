package ru.javawebinar.topjava.util.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;
import ru.javawebinar.topjava.util.DateTimeUtil;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class StringToLocalDate implements Converter<String, LocalDate> {
    DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @Override
    public @Nullable
    LocalDate convert(@Nullable String source) {
        return DateTimeUtil.parseLocalDate(source);
    }
}
