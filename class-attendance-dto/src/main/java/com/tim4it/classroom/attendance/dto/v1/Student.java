package com.tim4it.classroom.attendance.dto.v1;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

import java.time.LocalDate;
import java.util.List;

@Schema(description = "Student data")
@Value
@Builder(toBuilder = true)
@Jacksonized
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class Student {

    @Schema(description = "Id of a student")
    @Builder.Default
    String id = "";

    @Schema(description = "First name")
    @Builder.Default
    String firstName = "";

    @Schema(description = "Last name")
    @Builder.Default
    String lastName = "";

    @Schema(description = "Email")
    @Builder.Default
    String email = "";

    @Schema(description = "Phone")
    @Builder.Default
    String phone = "";

    @Schema(description = "Year of birth")
    LocalDate yearOfBirth;

    @Schema(description = "School Year")
    Integer year;

    @Schema(description = "Department")
    @Builder.Default
    String department = "";

    @Schema(description = "Department")
    @Builder.Default
    List<String> classroomAccess = List.of();
}
