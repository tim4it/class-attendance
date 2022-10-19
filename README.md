# Class attendance 
Class attendance project

# Build an testing

Build application with tests:

```asciidoc
./gradlew clean build
```

Build application - without tests:

```asciidoc
./gradlew clean build -x test
```

Run application - starts application on port 8080:
```asciidoc
./gradlew run
```

# Test

Run the tests:
```asciidoc
./gradlew clean build
```

Test reporting (open in web browser):
```asciidoc
build/reports/tests/test/index.html
```

# Test coverage

Run the tests:
```asciidoc
./gradlew clean build
```

Test coverage - jacoco (open in web browser):
```asciidoc
build/reports/jacoco/html/index.html
```

# Test application with swagger

Run application locally:
```asciidoc
./gradlew run
```
Open swagger documentation and testing:

```asciidoc
http://localhost:8080/swagger/views/swagger-ui/index.html
```
