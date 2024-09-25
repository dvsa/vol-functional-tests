FROM maven:3.8.1-amazoncorretto-8 as maven
WORKDIR /app
ADD . /app

RUN ["mvn", "clean", "package", "-P build-local -skipTests=true"]

ENV platform="LINUX"
ENV gridURL="https://selenium-hub.olcs.dev-dvsacloud.uk/"


ENV proxyHost=$proxyHost
ENV proxyPort=$proxyPort

WORKDIR /app

RUN yum update -y && \
    yum install -y zip-3.* unzip-6.*  && \
    yum clean all && \
    rm -rf /var/cache/yum

# Install latest version of the aws cli 
RUN curl "https://awscli.amazonaws.com/awscli-exe-linux-x86_64.zip" -o "awscliv2.zip" && \
    unzip awscliv2.zip && \
    ./aws/install --bin-dir /usr/local/bin --install-dir /usr/local/aws-cli --update && \
    rm -f awscliv2.zip


COPY ./settings.xml /root/.m2/settings.xml
COPY pom.xml ./

RUN mvn dependency:go-offline - B

COPY ./src ./src

RUN mvn package -DskipTests

FROM openjdk:11-jre-slim

ARG JAR_VERSION=2.40.8-SNAPSHOT

COPY --from=maven-build-stage /app/target/vol-functional-tests-${JAR_VERSION}.jar /app/test-artifact.jar

COPY run.sh /app/run.sh
RUN chmod +x ./run.sh

CMD ["/bin/sh", "-c", "./run.sh"]