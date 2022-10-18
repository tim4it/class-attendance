package com.tim4it.classroom.attendance.v1.entity;

import com.tim4it.classroom.attendance.dto.v1.Classroom;
import reactor.core.publisher.Mono;

public interface ClassRoom {

    Mono<Classroom> findById(String classroomId);
}
