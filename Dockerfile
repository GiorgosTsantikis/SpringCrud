# Use a base image with Java installed
FROM openjdk:23-jdk

# Set the working directory
WORKDIR /app

# Copy the JAR file (replace 'SpringCrud-0.0.1-SNAPSHOT.jar' with your JAR name)
COPY target/demo-0.0.1-SNAPSHOT.jar app.jar

# Expose the application's port
EXPOSE 8070

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
