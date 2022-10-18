package com.tim4it.classroom.attendance.v1.service;

import com.tim4it.classroom.attendance.db.MasterDB;
import jakarta.inject.Singleton;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

@Slf4j
@Singleton
public class AuthImpl implements Auth {

    @Override
    public Optional<String> verifyStudent(@NonNull String authorizationHeader) {
        return Optional.of("OK");
    }

    @Override
    public Optional<String> verifyAndGetStudentId(String authorizationHeader) {
        // retrieve fake student - student id must come from JWT token (best is custom token implementation)
        return Optional.of(MasterDB
                .getStudents()
                .values()
                .iterator().next()
                .getId());
    }
}
