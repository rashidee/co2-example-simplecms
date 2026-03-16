# Stage 1: Build frontend assets and Java application
FROM eclipse-temurin:21-jdk AS builder

# Install Maven and Node.js for frontend build
RUN apt-get update && apt-get install -y curl maven \
    && curl -fsSL https://deb.nodesource.com/setup_20.x | bash - \
    && apt-get install -y nodejs \
    && rm -rf /var/lib/apt/lists/*

WORKDIR /app

# Copy pom.xml first for dependency caching
COPY pom.xml ./
RUN mvn dependency:go-offline -B || true

# Copy frontend source and install dependencies
COPY frontend/ frontend/

# Copy application source
COPY src/ src/

# Copy JTE templates
COPY jte-classes/ jte-classes/

# Build the application (includes frontend build via frontend-maven-plugin)
RUN mvn clean package -DskipTests -B

# Stage 2: Runtime
FROM eclipse-temurin:21-jre

WORKDIR /app

# Copy the built JAR
COPY --from=builder /app/target/*.jar app.jar

# Create uploads directory
RUN mkdir -p /app/uploads

# Expose application port
EXPOSE 8080

# Environment variables (override at runtime for production)
ENV SPRING_DATASOURCE_URL=jdbc:postgresql://host.docker.internal:5432/cms_db \
    SPRING_DATASOURCE_USERNAME=cms_user \
    SPRING_DATASOURCE_PASSWORD=cms_password

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
