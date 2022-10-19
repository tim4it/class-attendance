package com.tim4it.classroom.attendance.dto.v1;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

@Schema(description = "Lecture data")
@Value
@Builder(toBuilder = true)
@Jacksonized
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class Lecture {

    @Schema(description = "Id of lecture")
    @Builder.Default
    String id = "";

    @Schema(description = "Professor full name")
    @Builder.Default
    String professor = "";

    @Schema(description = "Lecture main subject")
    Subject subject;

    @Schema(description = "Current subject topic")
    @Builder.Default
    String subjectTopic = "";

    @Schema(description = "Additional information about subject or subject topics")
    @Builder.Default
    String additionalInformation = "";

    @Schema(description = "Is recording of lecture available")
    boolean recording;

    @Schema(description = "Recording URL")
    @Builder.Default
    String recordingUrl = "";
}
