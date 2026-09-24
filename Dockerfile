FROM eclipse-temurin:17-jdk AS build
WORKDIR /app
COPY .mvn/ .mvn/
COPY mvnw pom.xml ./
RUN ./mvnw -q -B dependency:go-offline
COPY src ./src
RUN ./mvnw -q -B package -DskipTests

FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8081
# "secure" activo por defecto: requiere AZURE_TENANT_ID y AZURE_API_AUDIENCE.
# Para correr sin Azure (demo local): docker run -e SPRING_PROFILES_ACTIVE=default ...
ENV SPRING_PROFILES_ACTIVE=secure
ENTRYPOINT ["java", "-jar", "app.jar"]
