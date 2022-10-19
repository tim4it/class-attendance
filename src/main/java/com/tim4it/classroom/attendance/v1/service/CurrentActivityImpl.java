package com.tim4it.classroom.attendance.v1.service;

import com.tim4it.classroom.attendance.dto.v1.Classroom;
import com.tim4it.classroom.attendance.dto.v1.response.CurrentActivityData;
import com.tim4it.classroom.attendance.util.Helper;
import com.tim4it.classroom.attendance.util.Pair;
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
        return auth.verifyStudent(headers)
                .flatMap(studentVerified ->
                        Mono.zip(
                                classRoom.findById(classroomId),
                                Mono.just(studentVerified),
                                Pair::new))
                .filter(pair -> Helper.optString(pair.getFirst().getId()).isPresent())
                .switchIfEmpty(Mono
                        .error(new HttpStatusException(HttpStatus.BAD_REQUEST, "Invalid classroom data!")))
                .mapNotNull(pair -> timeBasedClassroom(pair.getFirst(), pair.getSecond(), time));
    }

    /**
     * Check all lectures and compare it with current time. Show remaining lectures - depends on current time
     *
     * @param classroom classroom
     * @param time      current time
     * @return current activity - shows different scheduled time compared to current time
     */
    private CurrentActivityData timeBasedClassroom(@NonNull Classroom classroom,
                                                   @NonNull com.tim4it.classroom.attendance.dto.v1.Student student,
                                                   @NonNull LocalDateTime time) {
        var classroomScheduledData = classroom.getScheduledData().stream()
                .sorted(Comparator.comparing(Classroom.ScheduledData::getStart))
                .collect(Collectors.toUnmodifiableList());
        var classroomScheduledDataLen = classroomScheduledData.size();
        // check if student has access to classroom
        var studentAccess = student.getClassroomAccess().stream()
                .filter(classroomAccess -> classroomAccess.equals(classroom.getId()))
                .findFirst();
        if (studentAccess.isEmpty()) {
            return CurrentActivityData.builder()
                    .description("You don't have access to selected classroom!")
                    .classroom(Classroom.builder().build())
                    .build();
        }
        // time based view to lectures in classroom
        if (time.isBefore(classroomScheduledData.get(0).getStart())) {
            return CurrentActivityData.builder()
                    .description("Whole day activity")
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
        return Optional.ofNullable(showLectures(classroom.getScheduledData(), time))
                .filter(Helper.IS_NOT_EMPTY_LIST)
                .map(classScheduled -> classroom.toBuilder().scheduledData(classScheduled).build())
                .map(classroomOK ->
                        CurrentActivityData.builder()
                                .description("Available lectures")
                                .classroom(classroomOK)
                                .build())
                .orElseGet(() ->
                        CurrentActivityData.builder()
                                .description("Whole day activity")
                                .classroom(classroom)
                                .build());
    }

    /**
     * Show lectures using comparator current time. Show only lectures that are from current time on
     *
     * @param scheduledData scheduled data
     * @param time          current time
     * @return optional scheduled data
     */
    private List<Classroom.ScheduledData> showLectures(@NonNull List<Classroom.ScheduledData> scheduledData,
                                                       @NonNull LocalDateTime time) {
        return scheduledData.stream()
                .filter(classRoomScheduled -> time.isBefore(classRoomScheduled.getEnd()))
                .collect(Collectors.toUnmodifiableList());
    }
}
