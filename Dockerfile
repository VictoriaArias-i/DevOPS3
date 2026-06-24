# Stage 1: Build
FROM maven:3.9.6-eclipse-temurin-21-alpine AS builder
WORKDIR /app

# Copiar pom.xml y descargar dependencias
COPY pom.xml .
RUN mvn dependency:go-offline -B -q

# Copiar código fuente y compilar
COPY src ./src
RUN mvn clean package -DskipTests -B -q

# Stage 2: Run
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# Copiar el JAR generado desde el builder
COPY --from=builder /app/target/*.jar app.jar

# Variables de entorno recomendadas
ENV JAVA_OPTS="-XX:+UseContainerSupport -XX:MaxRAMPercentage=75.0"

# Exponer puerto de la aplicación
EXPOSE 8080

# Validación de salud (útil para Kubernetes probes)
HEALTHCHECK --interval=30s --timeout=10s --start-period=20s --retries=3 \
  CMD curl -f http://localhost:8080/actuator/health || exit 1

# Ejecutar la aplicación
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
