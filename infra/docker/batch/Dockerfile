FROM maven:3.9.6-amazoncorretto-11 as maven_builder

ENV platform="LINUX"
ENV gridURL="https://selenium-hub.olcs.dev-dvsacloud.uk/"


ENV proxyHost=$proxyHost
ENV proxyPort=$proxyPort

WORKDIR /app

COPY pom.xml /app/pom.xml
COPY .m2/settings.xml /root/.m2/settings.xml
COPY src ./src

RUN mvn dependency:go-offline && \
    mvn clean package -DskipTests


FROM amazoncorretto:11-alpine
WORKDIR /app
COPY --from=maven_builder /app/target/*.jar app.jar
COPY run.sh .
RUN chmod +x ./run.sh

SHELL ["bin/ash", "-eo", "pipefail", "-c"]

# hadolint ignore=SC2034
RUN apk add --no-cache \
    zip=3.0-r10 | \
    unzip=6.0-r9 \
    curl=8.5.0-r0


# Install latest version of the aws cli
RUN curl "https://awscli.amazonaws.com/awscli-exe-linux-x86_64.zip" -o "awscliv2.zip" && \
    unzip awscliv2.zip && \
    ./aws/install --bin-dir /usr/local/bin --install-dir /usr/local/aws-cli --update && \
    rm -f awscliv2.zip


CMD ["/bin/sh", "-c", "./run.sh"]