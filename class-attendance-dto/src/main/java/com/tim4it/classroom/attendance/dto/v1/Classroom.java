package com.tim4it.classroom.attendance.dto.v1;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.NonNull;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

import java.time.LocalDateTime;
import java.util.List;

@Schema(description = "Classroom data")
@Value
@Builder(toBuilder = true)
@Jacksonized
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class Classroom {

    @Schema(description = "Id of a classroom")
    @Builder.Default
    String id = "";

    @Schema(description = "location")
    @Builder.Default
    String location = "";

    @Schema(description = "Building")
    @Builder.Default
    String building = "";

    @Schema(description = "Classroom number")
    int classroomNumber;

    @Schema(description = "Scheduled data")
    @Builder.Default
    List<ScheduledData> scheduledData = List.of();

    @Schema(description = "Classroom scheduled data")
    @Value
    @Builder(toBuilder = true)
    @Jacksonized
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class ScheduledData {

        @Schema(description = "Lecture data")
        @NonNull
        Lecture lecture;

        @Schema(description = "Start subject date time")
        @NonNull
        LocalDateTime start;

        @Schema(description = "End subject date time")
        @NonNull
        LocalDateTime end;
    }
}
