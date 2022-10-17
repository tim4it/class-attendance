package com.tim4it.classroom.attendance.v1.controller;

import com.tim4it.classroom.attendance.dto.v1.ClassroomCheckIn;
import com.tim4it.classroom.attendance.v1.service.CheckIn;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.multipart.CompletedFileUpload;
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
import org.reactivestreams.Publisher;
import reactor.core.publisher.Mono;

@Slf4j
@OpenAPIDefinition(info = @Info(description = "Check in controller - log in and check in into selected classroom"))
@Controller("/v1")
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class CheckInController {

    @NonNull
    final CheckIn checkIn;

    @Operation(summary = "Payment comparison - compare two files and provide reports")
    @ApiResponse(responseCode = "200", description = "Payment comparison succeed!",
            content = @Content(mediaType = MediaType.APPLICATION_JSON,
                    schema = @Schema(implementation = ClassroomCheckIn.class)))
    @ApiResponse(responseCode = "400", description = "Bad request")
    @Post(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA, produces = MediaType.APPLICATION_JSON)
    public Mono<ClassroomCheckIn> checkIn(Publisher<CompletedFileUpload> file) {
//        return checkIn.upload(file);
        return null;
    }
}
