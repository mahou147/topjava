package ru.javawebinar.topjava.util.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;
import ru.javawebinar.topjava.util.DateTimeUtil;

import java.time.LocalTime;

public class StringToLocalTime implements Converter<String, LocalTime> {

    @Override
    public @Nullable
    LocalTime convert(@Nullable String source) {
        return DateTimeUtil.parseLocalTime(source);
    }
}
