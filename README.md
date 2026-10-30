# Calculator DevOps

CSE 816 Software Production Engineering mini-project: a Java calculator REST API with an automated Jenkins pipeline and Docker Compose deployment.

The assignment requirements are in [SPE_Mini_Project.docx](SPE_Mini_Project.docx).

## Project scope

The assignment requires Spring Boot and Maven, four arithmetic endpoints, structured input validation, JUnit 5 tests, and an Actuator health endpoint. The Jenkins pipeline must check out, build, test, containerize, push to Docker Hub, deploy with Compose, verify health, and send build notifications.

Deployment must include the calculator and a second component, such as an Nginx reverse proxy. Submission includes a short PDF report, screenshots, and a live demonstration of automatic deployment after a GitHub push.

## Prerequisites

- JDK 17 and Maven 3.6.3 or newer for the application.
- Git and a GitHub repository for version control.
- Docker Engine and Docker Compose for container deployment.
- Jenkins with Java 17, Maven, Docker access, GitHub access, Docker Hub credentials, and email delivery configured.

Maven uses `JAVA_HOME`. Ensure `mvn -version` reports Java 17 or newer, even if `java -version` already does.

## Build and test

Set `JAVA_HOME` to your JDK 17 installation, then run:

```bash
mvn clean package
mvn test
java -jar target/calculator.jar --server.port=8081
```

The JUnit 5 unit and Spring MVC integration tests cover all operations, validation, division by zero, and health. Jenkins can publish the XML reports from `target/surefire-reports/TEST-*.xml`.

## API

| Operation | Request |
| --- | --- |
| Add | `GET /api/v1/add?a=2&b=3` |
| Subtract | `GET /api/v1/subtract?a=2&b=3` |
| Multiply | `GET /api/v1/multiply?a=2&b=3` |
| Divide | `GET /api/v1/divide?a=2&b=3` |
| Health | `GET /actuator/health` |

```bash
curl 'http://localhost:8081/api/v1/add?a=2&b=3'
curl 'http://localhost:8081/api/v1/divide?a=1&b=0'
curl 'http://localhost:8081/actuator/health'
```

Successful calculations return JSON:

```json
{"operation":"add","a":2.0,"b":3.0,"result":5.0}
```

Missing, empty, non-numeric, and non-finite parameters, division by zero, and numeric overflow return HTTP 400 with a problem-detail JSON body:

```json
{"type":"about:blank","title":"Bad Request","status":400,"detail":"Division by zero is not allowed.","instance":"/api/v1/divide"}
```

Calculations use Java `double` arithmetic. Decimal results follow floating-point precision, so values such as `0.1 + 0.2` may have small rounding differences. Unknown operations return HTTP 404.

Only project source, tests, deployment configuration, and project documentation belong in version control. Build output, credentials, local assistant files, and planning notes are excluded.
