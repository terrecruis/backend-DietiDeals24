FROM ubuntu:latest

# Install necessary packages
RUN apt update && apt-get update && apt-get install -y maven openjdk-8-jdk

# Copy the project files to the container
COPY . /usr/src/myapp

# Set the working directory
WORKDIR /usr/src/myapp

# Build the project with Maven
RUN mvn install

# Expose the port the application will run on
EXPOSE 8080

# Define the command to run the application
CMD ["java", "-jar", "target/backend.jar"]
