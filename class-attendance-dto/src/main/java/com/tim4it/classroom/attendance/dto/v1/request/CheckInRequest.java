package com.tim4it.classroom.attendance.dto.v1.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

@Schema(description = "Check in request")
@Value
@Builder(toBuilder = true)
@Jacksonized
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class CheckInRequest {

    @Schema(description = "Classroom id")
    @Builder.Default
    String classroomId = "";

    @Schema(description = "Lecture id")
    @Builder.Default
    String lectureId = "";

    @Schema(description = "Current time")
    @Builder.Default
    String time = "";
}
