# Class attendance project 

Design a system which allows students (system users) to register class attendance using QR codes. 
Assume that QR codes are posted on walls/desks (e.g. unique per classroom).
Class schedule is computerized and information about activities in each respective classroom is known to the system backend.
Goal is to design a backend API to do the following:

1. Retrieve current activities for a given QR code (effectively, classroom ID)
2. 'Check in' for a selected activity

Current activities are determined as follows: a class that is either about to start or just started (it's up to the implementor to reason & decide about time intervals to use).

Schedule implementation is out of scope for this assignment (e.g. scheduled activities can be mocked, but completed assignment should clearly demonstrate their usage).

QR code scanning is also out of scope.

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
