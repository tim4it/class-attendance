package com.tim4it.classroom.attendance.v1.controller

import com.tim4it.classroom.attendance.dto.v1.Subject
import com.tim4it.classroom.attendance.dto.v1.response.CurrentActivityData
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
class CurrentActivityControllerSpec extends Specification {

    @Inject
    @Client('/')
    HttpClient client

    def "Classroom activity controller - we don't have authorization data"() {
        given:
        def bodyTypeResponse = Argument.of(CurrentActivityData)
        def url = "/v1/current-activity?id=37f6a62e&time=2022-12-12%2012:30"
        def request = HttpRequest.GET(url)

        when:
        client.toBlocking().retrieve(request, bodyTypeResponse)

        then:
        def exception = thrown(HttpClientResponseException)
        exception.getStatus() == HttpStatus.UNAUTHORIZED
    }

    def "Classroom activity controller - authorization present - user not found"() {
        given:
        def bodyTypeResponse = Argument.of(CurrentActivityData)
        def url = "/v1/current-activity?id=b3ecbc16-4ec7-11ed-bdc3-0242ac260104&time=2022-12-12%2012:30"
        def request = HttpRequest.GET(url)
                                 .header(HttpHeaders.AUTHORIZATION, "33e6913a-4ec3-11ed-bdc3-0442ac120503")

        when:
        client.toBlocking().retrieve(request, bodyTypeResponse)

        then:
        def exception = thrown(HttpClientResponseException)
        exception.getStatus() == HttpStatus.UNAUTHORIZED
    }

    def "Classroom activity controller - authorization present, time is wierd"() {
        given:
        def bodyTypeResponse = Argument.of(CurrentActivityData)
        def url = "/v1/current-activity?id=b3ecbc16-4ec7-11ed-bdc3-0242ac260104&time=2022-12-12%2012:30"
        def request = HttpRequest.GET(url)
                                 .header(HttpHeaders.AUTHORIZATION, "33e6913a-4ec3-11ed-bdc3-0242ac120002")

        when:
        def body = client.toBlocking().retrieve(request, bodyTypeResponse)

        then:
        body
        body.getDescription() == "No lectures present for current day."
        body.getClassroom().getId() == "b3ecbc16-4ec7-11ed-bdc3-0242ac260104"
    }

    def "Classroom activity controller - authorization present, no access to classroom "() {
        given:
        def bodyTypeResponse = Argument.of(CurrentActivityData)
        def url = "/v1/current-activity?id=b3ecbc16-4ec7-11ed-bdc3-0242ac260104&time=2022-12-12%2012:30"
        def request = HttpRequest.GET(url)
                                 .header(HttpHeaders.AUTHORIZATION, "c8c9b0c0-4ec3-11ed-bdc3-0242ac120002")

        when:
        def body = client.toBlocking().retrieve(request, bodyTypeResponse)

        then:
        body
        body.getDescription() == "You don't have access to selected classroom!"
        body.getClassroom().getId() == ""
    }

    def "Classroom activity controller - authorization present, time before all lectures"() {
        given:
        def bodyTypeResponse = Argument.of(CurrentActivityData)
        def url = "/v1/current-activity?id=37f6a62e-4ec4-11ed-bdc3-0242ac120002&time=2022-10-20%2008:00"
        def request = HttpRequest.GET(url)
                                 .header(HttpHeaders.AUTHORIZATION, "33e6913a-4ec3-11ed-bdc3-0242ac120002")

        when:
        def body = client.toBlocking().retrieve(request, bodyTypeResponse)

        then:
        body
        body.getDescription() == "Whole day activity"
        with(body.getClassroom()) {
            id == "37f6a62e-4ec4-11ed-bdc3-0242ac120002"
            location == "Big hall"
            building == "Section C"
            classroomNumber == 16
            !scheduledData.isEmpty()
            scheduledData.size() == 2

            with(scheduledData.first()) {
                with(lecture) {
                    id == "ad6a4eb0-4ec4-11ed-bdc3-0242ac120002"
                    professor == "prof. Boris Tai"
                    subject == Subject.ALGEBRA
                    subjectTopic == "Linear systems"
                    recording
                }
            }
            with(scheduledData.last()) {
                with(lecture) {
                    id == "88c815aa-4ec5-11ed-bdc3-0242ac120002"
                    professor == "prof. Tanya Scoty"
                    subject == Subject.MATH
                    subjectTopic == "Set Theory"
                    recording
                }
            }
        }
    }

    def "Classroom activity controller - authorization present, lecture already started"() {
        given:
        def bodyTypeResponse = Argument.of(CurrentActivityData)
        def url = "/v1/current-activity?id=37f6a62e-4ec4-11ed-bdc3-0242ac120002&time=2022-10-20%2008:33"
        def request = HttpRequest.GET(url)
                                 .header(HttpHeaders.AUTHORIZATION, "33e6913a-4ec3-11ed-bdc3-0242ac120002")

        when:
        def body = client.toBlocking().retrieve(request, bodyTypeResponse)

        then:
        body
        body.getDescription() == "Available lectures"
        with(body.getClassroom()) {
            id == "37f6a62e-4ec4-11ed-bdc3-0242ac120002"
            location == "Big hall"
            building == "Section C"
            classroomNumber == 16
            !scheduledData.isEmpty()
            scheduledData.size() == 2
            with(scheduledData.first()) {
                with(lecture) {
                    id == "ad6a4eb0-4ec4-11ed-bdc3-0242ac120002"
                    professor == "prof. Boris Tai"
                    subject == Subject.ALGEBRA
                    subjectTopic == "Linear systems"
                    recording
                }
            }
        }
    }

    def "Classroom activity controller - authorization present, only one lecture left"() {
        given:
        def bodyTypeResponse = Argument.of(CurrentActivityData)
        def url = "/v1/current-activity?id=37f6a62e-4ec4-11ed-bdc3-0242ac120002&time=2022-10-20%2009:41"
        def request = HttpRequest.GET(url)
                                 .header(HttpHeaders.AUTHORIZATION, "33e6913a-4ec3-11ed-bdc3-0242ac120002")

        when:
        def body = client.toBlocking().retrieve(request, bodyTypeResponse)

        then:
        body
        body.getDescription() == "Available lectures"
        with(body.getClassroom()) {
            id == "37f6a62e-4ec4-11ed-bdc3-0242ac120002"
            location == "Big hall"
            building == "Section C"
            classroomNumber == 16
            !scheduledData.isEmpty()
            scheduledData.size() == 1
            with(scheduledData.first()) {
                with(lecture) {
                    id == "88c815aa-4ec5-11ed-bdc3-0242ac120002"
                    professor == "prof. Tanya Scoty"
                    subject == Subject.MATH
                    subjectTopic == "Set Theory"
                    recording
                }
            }
        }
    }

    def "Classroom activity controller - authorization present, different lecture already started"() {
        given:
        def bodyTypeResponse = Argument.of(CurrentActivityData)
        def url = "/v1/current-activity?id=37f6a62e-4ec4-11ed-bdc3-0242ac120002&time=2022-10-20%2011:29"
        def request = HttpRequest.GET(url)
                                 .header(HttpHeaders.AUTHORIZATION, "33e6913a-4ec3-11ed-bdc3-0242ac120002")

        when:
        def body = client.toBlocking().retrieve(request, bodyTypeResponse)

        then:
        body
        body.getDescription() == "Available lectures"
        with(body.getClassroom()) {
            id == "37f6a62e-4ec4-11ed-bdc3-0242ac120002"
            location == "Big hall"
            building == "Section C"
            classroomNumber == 16
            !scheduledData.isEmpty()
            scheduledData.size() == 1
            with(scheduledData.first()) {
                with(lecture) {
                    id == "88c815aa-4ec5-11ed-bdc3-0242ac120002"
                    professor == "prof. Tanya Scoty"
                    subject == Subject.MATH
                    subjectTopic == "Set Theory"
                    recording
                }
            }
        }
    }
}
