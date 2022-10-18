package com.tim4it.classroom.attendance.v1.service;

import com.tim4it.classroom.attendance.dto.v1.Classroom;
import com.tim4it.classroom.attendance.dto.v1.response.CurrentActivityData;
import com.tim4it.classroom.attendance.util.Helper;
import com.tim4it.classroom.attendance.v1.entity.ClassRoom;
import io.micronaut.http.HttpHeaders;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.exceptions.HttpStatusException;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Singleton
@AllArgsConstructor(onConstructor_ = @Inject)
public class CurrentActivityImpl implements CurrentActivity {

    @NonNull
    final Auth auth;

    @NonNull
    final ClassRoom classRoom;

    @Override
    public Mono<CurrentActivityData> get(@NonNull HttpHeaders headers,
                                         @NonNull String classroomId,
                                         @NonNull LocalDateTime time) {
        return Mono.fromCallable(() -> Helper.optString(headers.get(HttpHeaders.AUTHORIZATION)))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .switchIfEmpty(Mono
                        .error(new HttpStatusException(HttpStatus.UNAUTHORIZED, "Authorization header is missing!")))
                .map(auth::verifyStudent)
                .filter(Optional::isPresent)
                .switchIfEmpty(Mono
                        .error(new HttpStatusException(HttpStatus.UNAUTHORIZED, "Invalid authorization!")))
                .flatMap(userVerified -> classRoom.findById(classroomId))
                .filter(classRoom -> Helper.optString(classRoom.getId()).isPresent())
                .switchIfEmpty(Mono
                        .error(new HttpStatusException(HttpStatus.BAD_REQUEST, "Invalid classroom data!")))
                .mapNotNull(classRoomOK -> timeBasedClassroom(classRoomOK, time));
    }

    /**
     * Check all lectures and compare it with current time. It is not the whole implementation. Time is hard and
     * development is more complex. Missing if student is in between times from lectures... This can be implemented with
     * current max date and next start date in a loop.
     *
     * @param classroom classroom
     * @param time      current time
     * @return current activity - shows different scheduled time compared to current time
     */
    private CurrentActivityData timeBasedClassroom(@NonNull Classroom classroom,
                                                   @NonNull LocalDateTime time) {
        var classroomScheduledData = classroom.getScheduledData().stream()
                .sorted(Comparator.comparing(Classroom.ScheduledData::getStart))
                .collect(Collectors.toUnmodifiableList());
        var classroomScheduledDataLen = classroomScheduledData.size();
        if (time.isBefore(classroomScheduledData.get(0).getStart())) {
            return CurrentActivityData.builder()
                    .description("Whole day lectures")
                    .classroom(classroom)
                    .build();
        } else if (time.isAfter(classroomScheduledData.get(classroomScheduledDataLen - 1).getEnd())) {
            return CurrentActivityData.builder()
                    .description("No lectures present for current day.")
                    .classroom(classroom.toBuilder()
                            .scheduledData(List.of())
                            .build())
                    .build();
        }
        return classStarted(classroom.getScheduledData(), time)
                .map(classScheduled -> classroom.toBuilder().scheduledData(List.of(classScheduled)).build())
                .map(classroomOK ->
                        CurrentActivityData.builder()
                                .description("Lecture already started")
                                .classroom(classroomOK)
                                .build())
                .orElseGet(() ->
                        CurrentActivityData.builder()
                                .description("Whole day activity")
                                .classroom(classroom)
                                .build());
    }

    /**
     * Class that already started. Current time is in interval of started class
     *
     * @param scheduledData scheduled data
     * @param time          current time
     * @return optional scheduled data
     */
    private Optional<Classroom.ScheduledData> classStarted(@NonNull List<Classroom.ScheduledData> scheduledData,
                                                           @NonNull LocalDateTime time) {
        return scheduledData.stream()
                .filter(classRoomScheduled ->
                        time.isAfter(classRoomScheduled.getStart()) &&
                                time.isBefore(classRoomScheduled.getEnd()))
                .findFirst();
    }
}
