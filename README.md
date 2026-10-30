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

## Repository contents

This initial setup contains the assignment document, this README, and `.gitignore`. Build, test, API usage, and deployment instructions will accompany their implementations.

Only project source, tests, deployment configuration, and project documentation belong in version control. Build output, credentials, local assistant files, and planning notes are excluded.
