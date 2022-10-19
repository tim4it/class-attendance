package com.tim4it.classroom.attendance.v1.entity;

import reactor.core.publisher.Mono;

public interface Student {

    /**
     * Find student by id
     *
     * @param studentId student id
     * @return found student full object, or empty student object if student is not found
     */
    Mono<com.tim4it.classroom.attendance.dto.v1.Student> findById(String studentId);
}
