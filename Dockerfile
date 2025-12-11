# Stage 1: Build with Maven
FROM eclipse-temurin:17-jdk AS build

WORKDIR /app

# Copy Maven project files
COPY . .

# Build the project
RUN apt-get update && apt-get install -y maven && mvn clean package -DskipTests

# Stage 2: Run the Spring Boot JAR
FROM eclipse-temurin:17-jdk

WORKDIR /app

# Copy the built JAR from the build stage
COPY --from=build /app/target/SystemInfoFirebase-1.0-SNAPSHOT.jar app.jar

# Run the Spring Boot JAR
CMD ["java", "-jar", "app.jar"]
