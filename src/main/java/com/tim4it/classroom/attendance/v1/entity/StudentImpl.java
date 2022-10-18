package com.tim4it.classroom.attendance.v1.entity;

import com.tim4it.classroom.attendance.db.MasterDB;
import jakarta.inject.Singleton;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@Singleton
public class StudentImpl implements Student {

    @Override
    public Mono<com.tim4it.classroom.attendance.dto.v1.Student> findById(@NonNull String studentId) {
        return Mono.fromCallable(() ->
                MasterDB.getStudents()
                        .getOrDefault(studentId, com.tim4it.classroom.attendance.dto.v1.Student.builder().build()));
    }
}
