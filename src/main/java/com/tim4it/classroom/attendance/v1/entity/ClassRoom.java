package com.tim4it.classroom.attendance.v1.entity;

import com.tim4it.classroom.attendance.dto.v1.Classroom;
import reactor.core.publisher.Mono;

public interface ClassRoom {

    /**
     * Find classroom by id
     *
     * @param classroomId classroom id
     * @return found classroom full object, or empty classroom object if classroom is not found
     */
    Mono<Classroom> findById(String classroomId);
}
