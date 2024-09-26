FROM maven:3.9.6-amazoncorretto-11 as maven

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

COPY . .

COPY ./settings.xml /root/.m2/settings.xml

RUN mvn clean package -B -DskipTests -Dmaven.repo.local=/root/.m2/repository

RUN chmod +x ./run.sh

CMD ["/bin/sh", "-c", "./run.sh"]