package com.tim4it.classroom.attendance.db;

import com.tim4it.classroom.attendance.dto.v1.Classroom;
import com.tim4it.classroom.attendance.dto.v1.Lecture;
import com.tim4it.classroom.attendance.dto.v1.Student;
import com.tim4it.classroom.attendance.dto.v1.Subject;
import lombok.experimental.UtilityClass;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * Fake master DB - no-sql based - for this specific assignment it is very fast - O(1) access time for current API data
 */
@UtilityClass
public class MasterDB {

    static final Map<String, Student> students = Map.of(
            "33e6913a-4ec3-11ed-bdc3-0242ac120002",
            Student.builder()
                    .id("33e6913a-4ec3-11ed-bdc3-0242ac120002")
                    .firstName("John")
                    .lastName("Star")
                    .email("john.star@yahoo.com")
                    .phone("+442342387297")
                    .yearOfBirth(LocalDate.of(2000, 5, 13))
                    .year(2)
                    .department("Computer science")
                    .classroomAccess(List.of("b3ecbc16-4ec7-11ed-bdc3-0242ac260104", "37f6a62e-4ec4-11ed-bdc3-0242ac120002"))
                    .build(),
            "c8c9b0c0-4ec3-11ed-bdc3-0242ac120002",
            Student.builder()
                    .id("c8c9b0c0-4ec3-11ed-bdc3-0242ac120002")
                    .firstName("Mia")
                    .lastName("Bleed")
                    .email("mia.bleed@google.com")
                    .phone("+44349856648")
                    .yearOfBirth(LocalDate.of(1999, 3, 13))
                    .year(3)
                    .department("Chemistry")
                    .classroomAccess(List.of("6fc9218f-5eb3-4b28-8e88-11060870152d"))
                    .build(),
            "fea5cbfc-4ec3-11ed-bdc3-0242ac120002",
            Student.builder()
                    .id("fea5cbfc-4ec3-11ed-bdc3-0242ac120002")
                    .firstName("Sas")
                    .lastName("McCoon")
                    .email("sas.mccoon@google.com")
                    .phone("+442439857475548")
                    .yearOfBirth(LocalDate.of(2001, 7, 13))
                    .year(1)
                    .department("Biology")
                    .classroomAccess(List.of(""))
                    .build()
    );

    static final Map<String, Lecture> lectures = Map.of(
            "ad6a4eb0-4ec4-11ed-bdc3-0242ac120002",
            Lecture.builder()
                    .id("ad6a4eb0-4ec4-11ed-bdc3-0242ac120002")
                    .professor("prof. Boris Tai")
                    .subject(Subject.ALGEBRA)
                    .subjectTopic("Linear systems")
                    .additionalInformation("Algebra book page 45 - 55")
                    .recording(true)
                    .recordingUrl("https://schoole/data/12")
                    .build(),
            "88c815aa-4ec5-11ed-bdc3-0242ac120002",
            Lecture.builder()
                    .id("88c815aa-4ec5-11ed-bdc3-0242ac120002")
                    .professor("prof. Tanya Scoty")
                    .subject(Subject.MATH)
                    .subjectTopic("Set Theory")
                    .additionalInformation("Math book page 66 - 77")
                    .recording(true)
                    .recordingUrl("https://schoole/data/16")
                    .build(),
            "b408d61e-4ec5-11ed-bdc3-0242ac120002",
            Lecture.builder()
                    .id("b408d61e-4ec5-11ed-bdc3-0242ac120002")
                    .professor("prof. Tony Stark")
                    .subject(Subject.BIOLOGY)
                    .subjectTopic("Carbon Cycle")
                    .additionalInformation("Biology book page 88 - 100")
                    .recording(true)
                    .recordingUrl("https://schoole/data/18")
                    .build(),
            "0d017dac-4ec6-11ed-bdc3-0242ac120002",
            Lecture.builder()
                    .id("0d017dac-4ec6-11ed-bdc3-0242ac120002")
                    .professor("prof. Glenn Sciriozo")
                    .subject(Subject.CHEMISTRY)
                    .subjectTopic("Atomic Structure")
                    .additionalInformation("Chemistry book page 101 - 122")
                    .recording(true)
                    .recordingUrl("https://schoole/data/33")
                    .build(),
            "0a211dac-5ec7-14ab-ade9-0242ac120002",
            Lecture.builder()
                    .id("0a211dac-5ec7-14ab-ade9-0242ac120002")
                    .professor("prof. Maxy Baxy")
                    .subject(Subject.HISTORY)
                    .subjectTopic("Cold War")
                    .additionalInformation("History book page 102 - 120")
                    .recording(true)
                    .recordingUrl("https://schoole/data/43")
                    .build(),
            "ae9d7648-4ec6-11ed-bdc3-0242ac120002",
            Lecture.builder()
                    .id("ae9d7648-4ec6-11ed-bdc3-0242ac120002")
                    .professor("prof. Tia Scaza")
                    .subject(Subject.ENGLISH)
                    .subjectTopic("Social Impact of Covid-19")
                    .additionalInformation("English book page 77 - 87")
                    .recording(true)
                    .recordingUrl("https://schoole/data/26")
                    .build()
    );

    static final Map<String, Classroom> classrooms = Map.of(
            "37f6a62e-4ec4-11ed-bdc3-0242ac120002",
            Classroom.builder()
                    .id("37f6a62e-4ec4-11ed-bdc3-0242ac120002")
                    .location("Big hall")
                    .building("Section C")
                    .classroomNumber(16)
                    .scheduledData(List.of(
                            Classroom.ScheduledData.builder()
                                    .lecture(lectures.get("ad6a4eb0-4ec4-11ed-bdc3-0242ac120002"))
                                    .start(LocalDateTime.of(2022, 10, 20, 8, 15))
                                    .end(LocalDateTime.of(2022, 10, 20, 9, 15))
                                    .build(),
                            Classroom.ScheduledData.builder()
                                    .lecture(lectures.get("88c815aa-4ec5-11ed-bdc3-0242ac120002"))
                                    .start(LocalDateTime.of(2022, 10, 20, 10, 0))
                                    .end(LocalDateTime.of(2022, 10, 20, 11, 30))
                                    .build()))
                    .build(),
            "b3ecbc16-4ec7-11ed-bdc3-0242ac260104",
            Classroom.builder()
                    .id("b3ecbc16-4ec7-11ed-bdc3-0242ac260104")
                    .location("Small hall")
                    .building("Section F")
                    .classroomNumber(4)
                    .scheduledData(List.of(
                            Classroom.ScheduledData.builder()
                                    .lecture(lectures.get("b408d61e-4ec5-11ed-bdc3-0242ac120002"))
                                    .start(LocalDateTime.of(2022, 10, 21, 11, 30))
                                    .end(LocalDateTime.of(2022, 10, 21, 12, 0))
                                    .build(),
                            Classroom.ScheduledData.builder()
                                    .lecture(lectures.get("0d017dac-4ec6-11ed-bdc3-0242ac120002"))
                                    .start(LocalDateTime.of(2022, 10, 21, 12, 30))
                                    .end(LocalDateTime.of(2022, 10, 21, 13, 30))
                                    .build()))
                    .build(),
            "24ec68b1-4ec5-11ed-bdc1-0242ac790054",
            Classroom.builder()
                    .id("24ec68b1-4ec5-11ed-bdc1-0242ac790054")
                    .location("Medium hall")
                    .building("Section A")
                    .classroomNumber(9)
                    .scheduledData(List.of(
                            Classroom.ScheduledData.builder()
                                    .lecture(lectures.get("0a211dac-5ec7-14ab-ade9-0242ac120002"))
                                    .start(LocalDateTime.of(2022, 10, 21, 14, 30))
                                    .end(LocalDateTime.of(2022, 10, 21, 15, 15))
                                    .build(),
                            Classroom.ScheduledData.builder()
                                    .lecture(lectures.get("ae9d7648-4ec6-11ed-bdc3-0242ac120002"))
                                    .start(LocalDateTime.of(2022, 10, 21, 15, 40))
                                    .end(LocalDateTime.of(2022, 10, 21, 17, 0))
                                    .build()))
                    .build()
    );

    public Map<String, Student> getStudents() {
        return students;
    }

    public Map<String, Classroom> getClassrooms() {
        return classrooms;
    }

    public Map<String, Lecture> getLectures() {
        return lectures;
    }
}
