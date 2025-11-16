
FROM maven:3.9.9 AS build
WORKDIR /app
COPY pom.xml .
COPY /src ./src/
RUN mvn clean package -DskipTests

FROM bellsoft/liberica-openjdk-alpine:17 AS prod
WORKDIR /app
COPY --from=build /app/target/reactive-products-0.0.1-SNAPSHOT.jar reactive-products.jar
EXPOSE 9000

ENTRYPOINT ["java", "-jar", "reactive-products.jar"]