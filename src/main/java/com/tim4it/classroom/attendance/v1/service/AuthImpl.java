package com.tim4it.classroom.attendance.v1.service;

import com.tim4it.classroom.attendance.db.MasterDB;
import com.tim4it.classroom.attendance.util.Helper;
import com.tim4it.classroom.attendance.v1.entity.Student;
import io.micronaut.http.HttpHeaders;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.exceptions.HttpStatusException;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

import java.util.Optional;

@Slf4j
@Singleton
@AllArgsConstructor(onConstructor_ = @Inject)
public class AuthImpl implements Auth {

    @NonNull
    final Student student;

    @Override
    public Mono<com.tim4it.classroom.attendance.dto.v1.Student> verifyStudent(@NonNull HttpHeaders headers) {
        return Mono.fromCallable(() -> Helper.optString(headers.get(HttpHeaders.AUTHORIZATION)))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .switchIfEmpty(Mono
                        .error(new HttpStatusException(HttpStatus.UNAUTHORIZED, "Authorization header is missing!")))
                .map(this::verifyAndGetStudentId)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .switchIfEmpty(Mono
                        .error(new HttpStatusException(HttpStatus.UNAUTHORIZED, "Invalid authorization!")))
                .flatMap(student::findById)
                .filter(student -> Helper.optString(student.getId()).isPresent())
                .switchIfEmpty(Mono
                        .error(new HttpStatusException(HttpStatus.BAD_REQUEST, "Invalid student data!")));
    }

    @Override
    public Optional<String> verifyAndGetStudentId(@NonNull String authorizationHeader) {
        // retrieve fake student - student id must come from JWT token (best is custom token implementation)
        return Optional.of(MasterDB.getStudents())
                .map(studentMap ->
                        studentMap.getOrDefault(
                                authorizationHeader,
                                com.tim4it.classroom.attendance.dto.v1.Student.builder().build()))
                .flatMap(studentFound -> Helper.optString(studentFound.getId()));
    }
}
