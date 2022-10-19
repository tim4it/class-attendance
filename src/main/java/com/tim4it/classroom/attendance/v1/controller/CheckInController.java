package com.tim4it.classroom.attendance.v1.controller;

import com.tim4it.classroom.attendance.dto.v1.request.CheckInRequest;
import com.tim4it.classroom.attendance.dto.v1.response.ClassroomCheckIn;
import com.tim4it.classroom.attendance.util.Helper;
import com.tim4it.classroom.attendance.v1.service.CheckIn;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Post;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.inject.Inject;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@OpenAPIDefinition(info = @Info(description = "Check in controller - log in and check in to selected classroom, version v1"))
@Controller("/v1")
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class CheckInController {

    @NonNull
    final CheckIn checkIn;

    @Operation(summary = "Student check in to specific classroom (QR code) to specific lecture")
    @ApiResponse(responseCode = "200", description = "Check in succeed!",
            content = @Content(mediaType = MediaType.APPLICATION_JSON,
                    schema = @Schema(implementation = ClassroomCheckIn.class)))
    @ApiResponse(responseCode = "400", description = "Bad request")
    @ApiResponse(responseCode = "401", description = "Unauthorized")
    @ApiResponse(responseCode = "404", description = "Not found")
    @Post(value = "/check-in", consumes = MediaType.APPLICATION_JSON, produces = MediaType.APPLICATION_JSON)
    public Mono<ClassroomCheckIn> checkIn(@NonNull HttpRequest<?> request,
                                          @NonNull @Body CheckInRequest checkInRequest) {
        return Mono
                .fromCallable(() -> Helper.validateCurrentDateTime(checkInRequest.getTime()))
                .flatMap(currentTime ->
                        checkIn.save(request.getHeaders(),
                                checkInRequest.getClassroomId(),
                                checkInRequest.getLectureId(),
                                currentTime));
    }
}
