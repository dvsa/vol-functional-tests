FROM maven:3.9.6-amazoncorretto-11 as maven

ENV platform="LINUX"
ENV gridURL="https://selenium-hub.olcs.dev-dvsacloud.uk/"


ARG GITHUB_TOKEN
ARG CI_PROXY
ARG CI_PROXYPORT

ENV GITHUB_TOKEN=${GITHUB_TOKEN}
ENV http_proxy=http://${CI_PROXY}:${CI_PROXYPORT}
ENV https_proxy=http://${CI_PROXY}:${CI_PROXYPORT}

WORKDIR /app

COPY pom.xml /app/pom.xml
COPY .m2/settings.xml /root/.m2/settings.xml
COPY src ./src
RUN mvn dependency:go-offline -B -U -X - && mvn clean package -B -DskipTests -o -X


COPY src ./src

RUN yum update -y && \
    yum install -y zip-3.* unzip-6.*  && \
    yum clean all && \
    rm -rf /var/cache/yum

# Install latest version of the aws cli 
RUN curl "https://awscli.amazonaws.com/awscli-exe-linux-x86_64.zip" -o "awscliv2.zip" && \
    unzip awscliv2.zip && \
    ./aws/install --bin-dir /usr/local/bin --install-dir /usr/local/aws-cli --update && \
    rm -f awscliv2.zip

COPY . .


RUN chmod +x ./run.sh

CMD ["/bin/sh", "-c", "./run.sh"]