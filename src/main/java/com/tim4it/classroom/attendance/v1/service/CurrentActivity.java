package com.tim4it.classroom.attendance.v1.service;

import com.tim4it.classroom.attendance.dto.v1.response.CurrentActivityData;
import io.micronaut.http.HttpHeaders;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

public interface CurrentActivity {

    /**
     * Get current activity for specific classroom. This is basic implementation
     *
     * @param headers     headers
     * @param classroomId clasroom id
     * @param time        current time
     * @return current activity data if present
     */
    Mono<CurrentActivityData> get(HttpHeaders headers, String classroomId, LocalDateTime time);
}
