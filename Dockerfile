ARG BUILD_IMAGE=eclipse-temurin:21
ARG TEST_IMAGE=eclipse-temurin:21
ARG RUNTIME_IMAGE=eclipse-temurin:21-jre
ARG MAVEN_OPTS="-Xmx2000m"

FROM --platform=$BUILDPLATFORM $BUILD_IMAGE AS builder
ARG MAVEN_OPTS

WORKDIR /build

COPY .mvn /build/.mvn/
COPY mvnw pom.xml /build/

RUN ./mvnw -B de.qaware.maven:go-offline-maven-plugin:resolve-dependencies

COPY src /build/src
RUN ./mvnw -DskipTests -B package

# Integration tests
FROM --platform=$BUILDPLATFORM $TEST_IMAGE AS test
ARG MAVEN_OPTS

WORKDIR /build

COPY --from=builder /root/.m2/repository /root/.m2/repository
COPY --from=builder /build /build

RUN ./mvnw -B verify

# Build runtime image
FROM $RUNTIME_IMAGE

COPY --from=builder /build/target/shelly-exporter-*.jar shelly-exporter.jar
ENV JAVA_OPTS="-Xmx64m -Xms64m"
EXPOSE 8080
USER 65535:65535
CMD exec java ${JAVA_OPTS} -jar shelly-exporter.jar
