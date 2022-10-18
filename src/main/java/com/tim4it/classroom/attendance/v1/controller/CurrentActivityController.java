package com.tim4it.classroom.attendance.v1.controller;

import com.tim4it.classroom.attendance.dto.v1.response.CurrentActivityData;
import com.tim4it.classroom.attendance.util.Helper;
import com.tim4it.classroom.attendance.v1.service.CurrentActivity;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.QueryValue;
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
@OpenAPIDefinition(info = @Info(description = "Current activity controller V1"))
@Controller("/v1")
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class CurrentActivityController {

    @NonNull
    final CurrentActivity currentActivity;

    @Operation(summary = "Current activity")
    @ApiResponse(responseCode = "200", description = "Current activity data",
            content = @Content(mediaType = MediaType.APPLICATION_JSON,
                    schema = @Schema(implementation = CurrentActivityData.class)))
    @ApiResponse(responseCode = "400", description = "Bad request")
    @ApiResponse(responseCode = "401", description = "Unauthorized")
    @Get(value = "/current-activity", produces = MediaType.APPLICATION_JSON)
    public Mono<CurrentActivityData> currentActivity(@NonNull HttpRequest<?> request,
                                                     @QueryValue String id,
                                                     @QueryValue String time) {
        return Mono
                .fromCallable(() -> Helper.validateCurrentDateTime(time))
                .flatMap(currentTime -> currentActivity.get(request.getHeaders(), id, currentTime));
    }
}
