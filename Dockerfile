# Multi-stage Dockerfile for Java Spring Boot Application
# Stage 1: Build the application using Maven
# Stage 2: Create lightweight runtime image

# ============================================
# BUILD STAGE
# ============================================
FROM maven:3.9-eclipse-temurin-17-alpine AS builder

# Set working directory
WORKDIR /app

# Copy Maven configuration file
COPY pom.xml .

# Download dependencies (cached layer if pom.xml doesn't change)
RUN mvn dependency:go-offline -B

# Copy source code
COPY src ./src

# Build the application
RUN mvn clean package -DskipTests

# ============================================
# RUNTIME STAGE
# ============================================
FROM eclipse-temurin:17-jre-alpine

# Add metadata
LABEL maintainer="DevOps Team"
LABEL description="Java CI/CD Demo Application"
LABEL version="1.0.0"

# Create non-root user for security
RUN addgroup -S appgroup && adduser -S appuser -G appgroup

# Set working directory
WORKDIR /app

# Copy the JAR file from builder stage
COPY --from=builder /app/target/devops-demo.jar app.jar

# Change ownership to non-root user
RUN chown -R appuser:appgroup /app

# Switch to non-root user
USER appuser

# Expose application port
EXPOSE 8080

# Health check configuration
HEALTHCHECK --interval=30s --timeout=3s --start-period=40s --retries=3 \
  CMD wget --quiet --tries=1 --spider http://localhost:8080/health || exit 1

# Set JVM options for containerized environment
ENV JAVA_OPTS="-Xmx512m -Xms256m -XX:+UseContainerSupport -XX:MaxRAMPercentage=75.0"

# Run the application
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
