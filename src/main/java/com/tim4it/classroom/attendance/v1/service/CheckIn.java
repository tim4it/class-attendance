package com.tim4it.classroom.attendance.v1.service;

import com.tim4it.classroom.attendance.dto.v1.response.ClassroomCheckIn;
import io.micronaut.http.HttpHeaders;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

public interface CheckIn {

    /**
     * Save/check in current user. This is basic implementation
     *
     * @param headers     headers
     * @param classroomId classroom id
     * @param lectureId   lecture id
     * @param time        current time
     * @return classroom check in with additional description - tells whether student is successfully checked in
     */
    Mono<ClassroomCheckIn> save(HttpHeaders headers,
                                String classroomId,
                                String lectureId,
                                LocalDateTime time);
}
