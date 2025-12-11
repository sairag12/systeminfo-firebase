# Use an official OpenJDK image
FROM openjdk:17-jdk-slim

# Set working directory
WORKDIR /app

# Copy your Maven project files
COPY . .

# Build the project
RUN ./mvnw clean install

# Run the JAR file
CMD ["java", "-jar", "target/SystemInfoFirebase-1.0-SNAPSHOT.jar"]
