FROM openjdk:21

WORKDIR /app
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar

EXPOSE 10032

ENTRYPOINT ["java","-jar","/app/app.jar"]
