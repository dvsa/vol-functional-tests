# FROM ghcr.io/dvsa/dvsa-docker-images/java/11/corretto AS base

FROM maven:3.9.6-amazoncorretto-11 as maven

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

COPY ./vol-functional-tests .
COPY --from=maven-settings settings.xml ~/.m2/settings.xml

CMD ["/bin/sh", "-c", "./vol-functional-tests/run.sh"]


