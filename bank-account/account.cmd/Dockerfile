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

# Copiar y compilar account.cmd
COPY ./bank-account/account.cmd/pom.xml ./account.cmd/pom.xml
COPY ./bank-account/account.cmd/src ./account.cmd/src
RUN mvn clean package -DskipTests -f ./account.cmd/pom.xml

# Etapa de ejecución
FROM openjdk:16-jdk-alpine

WORKDIR /app

COPY --from=build /build/account.cmd/target/*.jar app.jar

EXPOSE 5000

ENTRYPOINT ["java", "-jar", "app.jar"]
