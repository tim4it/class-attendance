package com.tim4it.classroom.attendance.v1.controller

import com.tim4it.classroom.attendance.dto.v1.Subject
import com.tim4it.classroom.attendance.dto.v1.request.CheckInRequest
import com.tim4it.classroom.attendance.dto.v1.response.ClassroomCheckIn
import groovy.util.logging.Slf4j
import io.micronaut.core.type.Argument
import io.micronaut.http.HttpHeaders
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpStatus
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.http.client.exceptions.HttpClientResponseException
import io.micronaut.test.extensions.spock.annotation.MicronautTest
import jakarta.inject.Inject
import spock.lang.Specification
import spock.lang.Stepwise
import spock.lang.Unroll

@Slf4j
@Unroll
@Stepwise
@MicronautTest
class CheckInControllerSpec extends Specification {

    @Inject
    @Client('/')
    HttpClient client

    def "Check in controller - we don't have authorization data"() {
        given:
        def bodyTypeResponse = Argument.of(ClassroomCheckIn)
        def url = "/v1/check-in"
        def checkInReq = CheckInRequest.builder()
                                       .classroomId("b3ecbc16-4ec7-11ed-bdc3-0242ac260104")
                                       .lectureId("0d017dac-4ec6-11ed-bdc3-0242ac120002")
                                       .time("2022-12-12 08:22")
                                       .build()
        def request = HttpRequest.POST(url, checkInReq)

        when:
        client.toBlocking().retrieve(request, bodyTypeResponse)

        then:
        def exception = thrown(HttpClientResponseException)
        exception.getStatus() == HttpStatus.UNAUTHORIZED
    }

    def "Check in controller - student don't have right classroom data"() {
        given:
        def bodyTypeResponse = Argument.of(ClassroomCheckIn)
        def url = "/v1/check-in"
        def checkInReq = CheckInRequest.builder()
                                       .classroomId("b3ecbc16-4ec7-11ed-bdc3-0242ac260105")
                                       .lectureId("0d017dac-4ec6-11ed-bdc3-0242ac120002")
                                       .time("2022-12-12 08:22")
                                       .build()
        def request = HttpRequest.POST(url, checkInReq)
                                 .header(HttpHeaders.AUTHORIZATION, "33e6913a-4ec3-11ed-bdc3-0242ac120002")

        when:
        client.toBlocking().retrieve(request, bodyTypeResponse)

        then:
        def exception = thrown(HttpClientResponseException)
        exception.getStatus() == HttpStatus.BAD_REQUEST
    }

    def "Check in controller - wrong timing - after specific lecture"() {
        given:
        def bodyTypeResponse = Argument.of(ClassroomCheckIn)
        def url = "/v1/check-in"
        def checkInReq = CheckInRequest.builder()
                                       .classroomId("b3ecbc16-4ec7-11ed-bdc3-0242ac260104")
                                       .lectureId("0d017dac-4ec6-11ed-bdc3-0242ac120002")
                                       .time("2022-12-12 08:22")
                                       .build()
        def request = HttpRequest.POST(url, checkInReq)
                                 .header(HttpHeaders.AUTHORIZATION, "33e6913a-4ec3-11ed-bdc3-0242ac120002")

        when:
        def result = client.toBlocking().retrieve(request, bodyTypeResponse)

        then:
        result
        result.description == "Can't check in to classroom - lecture already finished!"
        result.lecture.id == ""
    }

    def "Check in controller - timing OK - before lecture"() {
        given:
        def bodyTypeResponse = Argument.of(ClassroomCheckIn)
        def url = "/v1/check-in"
        def checkInReq = CheckInRequest.builder()
                                       .classroomId("b3ecbc16-4ec7-11ed-bdc3-0242ac260104")
                                       .lectureId("0d017dac-4ec6-11ed-bdc3-0242ac120002")
                                       .time("2022-10-21 12:26")
                                       .build()
        def request = HttpRequest.POST(url, checkInReq)
                                 .header(HttpHeaders.AUTHORIZATION, "33e6913a-4ec3-11ed-bdc3-0242ac120002")

        when:
        def result = client.toBlocking().retrieve(request, bodyTypeResponse)

        then:
        result
        result.description == "Check in OK"
        with(result.lecture) {
            id == "0d017dac-4ec6-11ed-bdc3-0242ac120002"
            subject == Subject.CHEMISTRY
            subjectTopic == "Atomic Structure"
            professor == "prof. Glenn Sciriozo"
            recording
        }
    }

    def "Check in controller - wrong timing - before specific lecture"() {
        given:
        def bodyTypeResponse = Argument.of(ClassroomCheckIn)
        def url = "/v1/check-in"
        def checkInReq = CheckInRequest.builder()
                                       .classroomId("b3ecbc16-4ec7-11ed-bdc3-0242ac260104")
                                       .lectureId("0d017dac-4ec6-11ed-bdc3-0242ac120002")
                                       .time("2022-10-21 12:15")
                                       .build()
        def request = HttpRequest.POST(url, checkInReq)
                                 .header(HttpHeaders.AUTHORIZATION, "33e6913a-4ec3-11ed-bdc3-0242ac120002")

        when:
        def result = client.toBlocking().retrieve(request, bodyTypeResponse)

        then:
        result
        result.description == "Can't check in to classroom - check in is valid 10 minutes before lecture!"
        result.lecture.id == ""
    }

    def "Check in controller - wrong timing - too long delay for specific lecture"() {
        given:
        def bodyTypeResponse = Argument.of(ClassroomCheckIn)
        def url = "/v1/check-in"
        def checkInReq = CheckInRequest.builder()
                                       .classroomId("b3ecbc16-4ec7-11ed-bdc3-0242ac260104")
                                       .lectureId("0d017dac-4ec6-11ed-bdc3-0242ac120002")
                                       .time("2022-10-21 12:52")
                                       .build()
        def request = HttpRequest.POST(url, checkInReq)
                                 .header(HttpHeaders.AUTHORIZATION, "33e6913a-4ec3-11ed-bdc3-0242ac120002")

        when:
        def result = client.toBlocking().retrieve(request, bodyTypeResponse)

        then:
        result
        result.description == "Can't check in to classroom - check in is valid 15 minutes after lecture start!"
        result.lecture.id == ""
    }

    def "Check in controller - timing ok - slight delay for specific lecture"() {
        given:
        def bodyTypeResponse = Argument.of(ClassroomCheckIn)
        def url = "/v1/check-in"
        def checkInReq = CheckInRequest.builder()
                                       .classroomId("b3ecbc16-4ec7-11ed-bdc3-0242ac260104")
                                       .lectureId("0d017dac-4ec6-11ed-bdc3-0242ac120002")
                                       .time("2022-10-21 12:40")
                                       .build()
        def request = HttpRequest.POST(url, checkInReq)
                                 .header(HttpHeaders.AUTHORIZATION, "33e6913a-4ec3-11ed-bdc3-0242ac120002")

        when:
        def result = client.toBlocking().retrieve(request, bodyTypeResponse)

        then:
        result
        result.description == "Check in OK"
        with(result.lecture) {
            id == "0d017dac-4ec6-11ed-bdc3-0242ac120002"
            subject == Subject.CHEMISTRY
            subjectTopic == "Atomic Structure"
            professor == "prof. Glenn Sciriozo"
            recording
        }
    }

    def "Check in controller - student has no access to classroom"() {
        given:
        def bodyTypeResponse = Argument.of(ClassroomCheckIn)
        def url = "/v1/check-in"
        def checkInReq = CheckInRequest.builder()
                                       .classroomId("b3ecbc16-4ec7-11ed-bdc3-0242ac260104")
                                       .lectureId("0d017dac-4ec6-11ed-bdc3-0242ac120002")
                                       .time("2022-10-21 12:40")
                                       .build()
        def request = HttpRequest.POST(url, checkInReq)
                                 .header(HttpHeaders.AUTHORIZATION, "c8c9b0c0-4ec3-11ed-bdc3-0242ac120002")

        when:
        def result = client.toBlocking().retrieve(request, bodyTypeResponse)

        then:
        result
        result.description == "You don't have access to selected classroom!"
        result.lecture.id == ""
    }

    def "Check in controller - wrong lecture data"() {
        given:
        def bodyTypeResponse = Argument.of(ClassroomCheckIn)
        def url = "/v1/check-in"
        def checkInReq = CheckInRequest.builder()
                                       .classroomId("b3ecbc16-4ec7-11ed-bdc3-0242ac260104")
                                       .lectureId("0d017dac-4ec6-11ed-bdc3-0242ac260602")
                                       .time("2022-10-21 12:40")
                                       .build()
        def request = HttpRequest.POST(url, checkInReq)
                                 .header(HttpHeaders.AUTHORIZATION, "c8c9b0c0-4ec3-11ed-bdc3-0242ac120002")

        when:
        def result = client.toBlocking().retrieve(request, bodyTypeResponse)

        then:
        result
        result.description == "Can't check in - wrong lecture data selected!"
        result.lecture.id == ""
    }
}
