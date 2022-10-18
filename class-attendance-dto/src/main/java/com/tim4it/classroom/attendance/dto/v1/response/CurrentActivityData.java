package com.tim4it.classroom.attendance.dto.v1.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.tim4it.classroom.attendance.dto.v1.Classroom;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

@Schema(description = "Classroom activity")
@Value
@Builder(toBuilder = true)
@Jacksonized
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class CurrentActivityData {

    @Schema(description = "Activity description")
    @Builder.Default
    String description = "";

    @Schema(description = "Classroom data")
    @Builder.Default
    Classroom classroom = Classroom.builder().build();
}
