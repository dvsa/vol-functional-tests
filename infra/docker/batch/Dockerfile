FROM maven:3.9.6-amazoncorretto-17 as maven

ENV platform="LINUX"
ENV gridURL="https://selenium-hub.olcs.dev-dvsacloud.uk/"

ENV proxyHost=$proxyHost
ENV proxyPort=$proxyPort

ENV mavenVersion=3.9.6
ENV M2_HOME=/opt/apache-maven-${mavenVersion}
ENV M2=$M2_HOME/bin

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
COPY pom.xml .

COPY .m2 /root/.m2

RUN mvn dependency:go-offline

COPY . .

RUN mvn package

RUN chmod +x ./run.sh

CMD ["/bin/sh", "-c", "./run.sh"]
