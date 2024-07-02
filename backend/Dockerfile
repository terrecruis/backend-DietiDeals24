FROM ubuntu:latest
RUN apt update
RUN apt-get update
RUN apt-get install -y maven
RUN apt-get install -y openjdk-8-jdk
COPY . /usr/src/myapp
EXPOSE 8080
WORKDIR /usr/src/myapp
RUN mvn install
CMD ["java", "-jar", "target/backend.jar"]