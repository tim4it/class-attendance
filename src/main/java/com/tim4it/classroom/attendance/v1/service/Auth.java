package com.tim4it.classroom.attendance.v1.service;

import java.util.Optional;

public interface Auth {

    /**
     * Verify if we have correct user - the best way is with JWT custom token. This should be separate service for
     * checking JWT tokens and JWT validity
     *
     * @param authorizationHeader authorization header value - JWT based
     * @return optional data - this is mock data
     */
    Optional<String> verifyStudent(String authorizationHeader);

    /**
     * Verify JWT token and get student ida data. Student id must come from JWT token
     *
     * @param authorizationHeader authorization header
     * @return student data comes from JWT token
     */
    Optional<String> verifyAndGetStudentId(String authorizationHeader);
}
