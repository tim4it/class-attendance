package com.tim4it.classroom.attendance.v1.entity;

import reactor.core.publisher.Mono;

public interface Student {

    Mono<com.tim4it.classroom.attendance.dto.v1.Student> findById(String studentId);
}
