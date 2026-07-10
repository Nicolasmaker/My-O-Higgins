# ============================================================
# MS-Mensajeria — imagen multi-stage (build Maven + runtime JRE)
# ============================================================

# ---- Build ----
FROM maven:3.9-eclipse-temurin-25 AS build
WORKDIR /app

# Cachea dependencias: solo se vuelven a descargar si cambia el pom.xml
COPY pom.xml .
RUN mvn -B -q dependency:go-offline || true

# Compila y empaqueta el jar (sin tests para acelerar el build)
COPY src ./src
RUN mvn -B -q clean package -DskipTests

# ---- Runtime ----
FROM eclipse-temurin:25-jre
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar

EXPOSE 8089
ENTRYPOINT ["java", "-jar", "app.jar"]
