FROM maven:3.8.1-amazoncorretto-11 as maven
WORKDIR /app
ADD . /app
COPY ./settings.xml /root/.m2/settings.xml
COPY pom.xml ./
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

COPY . .

RUN mvn dependency:go-offline && mvn package -DskipTests


COPY run.sh /app/run.sh
RUN chmod +x ./run.sh

CMD ["/bin/sh", "-c", "./run.sh"]