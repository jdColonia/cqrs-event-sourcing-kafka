# Etapa de compilación
FROM maven:3.8-openjdk-16 AS build

WORKDIR /build

# Copiar y compilar cqrs.core
COPY ./cqrs-es/cqrs.core/pom.xml ./cqrs.core/pom.xml
COPY ./cqrs-es/cqrs.core/src ./cqrs.core/src
RUN mvn clean install -DskipTests -f ./cqrs.core/pom.xml

# Copiar y compilar account.common
COPY ./bank-account/account.common/pom.xml ./account.common/pom.xml
COPY ./bank-account/account.common/src ./account.common/src
RUN mvn clean install -DskipTests -f ./account.common/pom.xml

# Copiar y compilar account.query
COPY ./bank-account/account.query/pom.xml ./account.query/pom.xml
COPY ./bank-account/account.query/src ./account.query/src
RUN mvn clean package -DskipTests -f ./account.query/pom.xml

# Etapa de ejecución
FROM openjdk:16-jdk-alpine

WORKDIR /app

COPY --from=build /build/account.query/target/*.jar app.jar

EXPOSE 5001

ENTRYPOINT ["java", "-jar", "app.jar"]
