# Use an official OpenJDK image
FROM eclipse-temurin:17-jdk

# Install Maven
RUN apt-get update && apt-get install -y maven

# Set working directory
WORKDIR /app

# Copy your Maven project files
COPY . .

# Build the project with Maven
RUN mvn clean install

# Run the JAR file
CMD ["java", "-jar", "target/SystemInfoFirebase-1.0-SNAPSHOT.jar"]
