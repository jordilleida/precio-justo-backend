FROM openjdk:18-jdk-slim
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar
EXPOSE 18085
ENTRYPOINT ["java","-jar","/app.jar"]