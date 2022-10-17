package com.tim4it.classroom.attendance;

import io.micronaut.context.annotation.Factory;
import io.micronaut.runtime.Micronaut;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;

@Factory
@OpenAPIDefinition(info = @Info(
        title = "Classroom attendance application",
        description = "Classroom attendance application - retrieve activities and perform check in",
        version = "1"))
public class Application {
    public static void main(String[] args) {
        Micronaut.run(Application.class, args);
    }
}
