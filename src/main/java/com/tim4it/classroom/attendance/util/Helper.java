package com.tim4it.classroom.attendance.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.exceptions.HttpStatusException;
import lombok.NonNull;
import lombok.experimental.UtilityClass;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;

@UtilityClass
public class Helper {
    static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public final Predicate<List<?>> IS_NOT_EMPTY_LIST = Predicate.not(List::isEmpty);
    public final Predicate<Map<?, ?>> IS_NOT_EMPTY_MAP = Predicate.not(Map::isEmpty);
    public final int CHECK_IN_BEFORE_MINUTES = 10;
    public final int CHECK_IN_STARTED_MINUTES = 15;

    /**
     * Nullable, trimmed and check if string is empty
     *
     * @param value value String
     * @return Optional String data
     */
    public Optional<String> optString(String value) {
        return Optional.ofNullable(value)
                .map(String::trim)
                .filter(valueCheck -> !valueCheck.isEmpty());
    }

    /**
     * Validate current time from String to LocalDateTime. Here we use local date time - it is simplified only for this
     * assignment. Otherwise, we use Zoned date time - date time is hard...
     *
     * @param time time in String format - human-readable
     * @return converted date time to local date time
     */
    public LocalDateTime validateCurrentDateTime(@NonNull String time) {
        try {
            return LocalDateTime.parse(time, FORMATTER);
        } catch (Exception e) {
            throw new HttpStatusException(HttpStatus.BAD_REQUEST,
                    "Wrong date time format. Must be in format: 'yyyy-MM-dd HH:mm'! " + e.getMessage());
        }
    }

    public String jsonToString(Object clazz) {
        try {
            var mapper = new ObjectMapper();
            mapper.enable(SerializationFeature.INDENT_OUTPUT);
            return new String(mapper.writeValueAsBytes(clazz), StandardCharsets.UTF_8);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
