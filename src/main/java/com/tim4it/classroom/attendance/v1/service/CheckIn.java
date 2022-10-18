package com.tim4it.classroom.attendance.v1.service;

import com.tim4it.classroom.attendance.dto.v1.response.ClassroomCheckIn;
import io.micronaut.http.HttpHeaders;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

public interface CheckIn {

    Mono<ClassroomCheckIn> save(HttpHeaders headers,
                                String classroomId,
                                String lectureId,
                                LocalDateTime time);
}
