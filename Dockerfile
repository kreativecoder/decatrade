FROM node as frontend
WORKDIR /frontend
COPY frontend .
RUN yarn install
RUN yarn build

FROM maven:3-jdk-8 as backend
WORKDIR /backend

COPY pom.xml .
RUN mvn dependency:go-offline -B

COPY src src

RUN mkdir -p src/main/resources/static
COPY --from=frontend /frontend/build src/main/resources/static
RUN mvn package -P docker

FROM openjdk:8-jdk-alpine
COPY --from=backend /backend/target/decatrade-0.0.1-SNAPSHOT.jar ./app.jar
EXPOSE 5000
RUN adduser -D user
USER user
CMD [ "sh", "-c", "java -Djava.security.egd=file:/dev/./urandom -jar app.jar" ]