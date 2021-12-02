package ru.javawebinar.topjava.util.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;
import ru.javawebinar.topjava.util.DateTimeUtil;

import java.time.LocalDate;

public class StringToLocalDate implements Converter<String, LocalDate> {

    @Override
    public @Nullable
    LocalDate convert(@Nullable String source) {
        return DateTimeUtil.parseLocalDate(source);
    }
}
