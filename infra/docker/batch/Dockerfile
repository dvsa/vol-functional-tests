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


FROM amazonlinux:2
WORKDIR /app
COPY --from=maven_builder /app/target/*.jar app.jar
COPY run.sh .
RUN chmod +x ./run.sh


RUN yum update -y && \
    yum install -y \
        zip \
        unzip \
        curl \
        awscli && \
    yum clean all && \
    rm -rf /var/cache/yum


## Install latest version of the aws cli
#RUN curl "https://awscli.amazonaws.com/awscli-exe-linux-x86_64.zip" -o "awscliv2.zip" && \
#    unzip awscliv2.zip && \
#    ./aws/install --bin-dir /usr/local/bin --install-dir /usr/local/aws-cli --update && \
#    rm -f awscliv2.zip


CMD ["/bin/bash", "-c", "./run.sh"]