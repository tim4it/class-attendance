package com.tim4it.classroom.attendance.v1.service;

import com.tim4it.classroom.attendance.dto.v1.Student;
import io.micronaut.http.HttpHeaders;
import lombok.NonNull;
import reactor.core.publisher.Mono;

import java.util.Optional;

public interface Auth {

    /**
     * Verify if we have correct student - the best way is with JWT custom token. This should be separate service for
     * checking JWT tokens and JWT validity
     *
     * @param headers http request headers
     * @return optional data - this is mock data
     */
    Mono<Student> verifyStudent(@NonNull HttpHeaders headers);

    /**
     * Verify JWT token and get student id data. Student id must come from JWT token
     *
     * @param authorizationHeader authorization header
     * @return student data comes from JWT token
     */
    Optional<String> verifyAndGetStudentId(String authorizationHeader);
}
