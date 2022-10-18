package com.tim4it.classroom.attendance.dto.v1.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.tim4it.classroom.attendance.dto.v1.Lecture;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.NonNull;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

@Schema(description = "Check in additional data")
@Value
@Builder(toBuilder = true)
@Jacksonized
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ClassroomCheckIn {

    @Schema(description = "Check in description")
    @Builder.Default
    String description = "";

    @Schema(description = "Lecture data")
    @NonNull
    Lecture lecture;
}
