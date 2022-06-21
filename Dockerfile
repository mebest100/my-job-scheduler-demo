FROM openjdk:8-jdk-alpine
RUN mkdir /usr/local/mytest && cd /usr/local/mytest && touch jsonData.json
WORKDIR /usr/local/mytest
COPY target/jobs-scheduler-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java","-jar","app.jar"]