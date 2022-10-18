package com.tim4it.classroom.attendance.v1.entity;

import com.tim4it.classroom.attendance.db.MasterDB;
import com.tim4it.classroom.attendance.dto.v1.Classroom;
import jakarta.inject.Singleton;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@Singleton
public class ClassRoomImpl implements ClassRoom {

    @Override
    public Mono<Classroom> findById(@NonNull String classroomId) {

        return Mono.fromCallable(() ->
                MasterDB.getClassrooms()
                        .getOrDefault(classroomId, Classroom.builder().build()));
    }
}
