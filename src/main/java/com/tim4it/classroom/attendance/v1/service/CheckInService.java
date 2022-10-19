package com.tim4it.classroom.attendance.v1.service;

import com.tim4it.classroom.attendance.dto.v1.Classroom;
import com.tim4it.classroom.attendance.dto.v1.Lecture;
import com.tim4it.classroom.attendance.dto.v1.response.ClassroomCheckIn;
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

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;

@Slf4j
@Singleton
@AllArgsConstructor(onConstructor_ = @Inject)
public class CheckInService implements CheckIn {

    @NonNull
    final Auth auth;

    @NonNull
    final ClassRoom classRoom;

    @Override
    public Mono<ClassroomCheckIn> save(@NonNull HttpHeaders headers,
                                       @NonNull String classroomId,
                                       @NonNull String lectureId,
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
                .mapNotNull(pair -> validateLectureTime(pair.getFirst(), pair.getSecond(), lectureId, time));
    }

    /**
     * Validate lecture from classroom and validate time
     *
     * @param classroom classroom data
     * @param lectureId lecture id
     * @param time      current time
     * @return classroom check in data - only if student can check in
     */
    private ClassroomCheckIn validateLectureTime(@NonNull Classroom classroom,
                                                 @NonNull com.tim4it.classroom.attendance.dto.v1.Student student,
                                                 @NonNull String lectureId,
                                                 @NonNull LocalDateTime time) {
        // check classroom lecture id with provided lecture id
        var lectureOpt = classroom.getScheduledData().stream()
                .filter(scheduledData ->
                        Optional.ofNullable(scheduledData.getLecture().getId())
                                .filter(lectureIdCheck -> lectureIdCheck.equals(lectureId))
                                .isPresent())
                .findFirst();
        if (lectureOpt.isEmpty()) {
            return ClassroomCheckIn.builder()
                    .description("Can't check in - wrong lecture data selected!")
                    .lecture(Lecture.builder().build())
                    .build();
        }

        // check if student has access to classroom
        var studentAccess = student.getClassroomAccess().stream()
                .filter(classroomAccess -> classroomAccess.equals(classroom.getId()))
                .findFirst();
        if (studentAccess.isEmpty()) {
            return ClassroomCheckIn.builder()
                    .description("You don't have access to selected classroom!")
                    .lecture(Lecture.builder().build())
                    .build();
        }

        // time based classroom check in
        var lecture = lectureOpt.get();
        if (time.isBefore(lecture.getStart())) {
            return checkInBeforeLectureStart(time, lecture);
        } else if (time.isAfter(lecture.getStart()) && time.isBefore(lecture.getEnd())) {
            return checkInLectureStarted(time, lecture);
        } else if (time.isAfter(lecture.getEnd())) {
            return ClassroomCheckIn.builder()
                    .description("Can't check in to classroom - lecture already finished!")
                    .lecture(Lecture.builder().build())
                    .build();
        }
        throw new HttpStatusException(HttpStatus.NOT_FOUND, "Lecture data not found!");
    }

    /**
     * Lecture check in if time is before lecture start. Student can check in 10 minutes before lecture starts
     *
     * @param time    current time
     * @param lecture selected lecture
     * @return classroom check in with message
     */
    private ClassroomCheckIn checkInBeforeLectureStart(@NonNull LocalDateTime time,
                                                       @NonNull Classroom.ScheduledData lecture) {
        var duration = Duration.between(time, lecture.getStart());
        var diff = Math.abs(duration.toMinutes());
        if (diff > Helper.CHECK_IN_BEFORE_MINUTES) {
            return ClassroomCheckIn.builder()
                    .description("Can't check in to classroom - check in is valid " + Helper.CHECK_IN_BEFORE_MINUTES +
                            " minutes before lecture!")
                    .lecture(Lecture.builder().build())
                    .build();
        }
        return ClassroomCheckIn.builder()
                .description("Check in OK")
                .lecture(lecture.getLecture())
                .build();
    }

    /**
     * Lecture check in if time is after lecture starts. Students can check in only if delay is 15 minutes after lecture
     * starts
     *
     * @param time    current time
     * @param lecture selected lecture
     * @return classroom check in with message
     */
    private ClassroomCheckIn checkInLectureStarted(@NonNull LocalDateTime time,
                                                   @NonNull Classroom.ScheduledData lecture) {
        var duration = Duration.between(lecture.getStart(), time);
        var diff = Math.abs(duration.toMinutes());
        if (diff > Helper.CHECK_IN_STARTED_MINUTES) {
            return ClassroomCheckIn.builder()
                    .description("Can't check in to classroom - check in is valid " + Helper.CHECK_IN_STARTED_MINUTES +
                            " minutes after lecture start!")
                    .lecture(Lecture.builder().build())
                    .build();
        }
        return ClassroomCheckIn.builder()
                .description("Check in OK")
                .lecture(lecture.getLecture())
                .build();
    }
}
