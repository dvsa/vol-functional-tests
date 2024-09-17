FROM 054614622558.dkr.ecr.eu-west-1.amazonaws.com/dvsa/vol/ci/jdk11:latest

VOLUME /root/.m2/repository

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

COPY run.sh /usr/local/bin/run.sh
RUN chmod +x /usr/local/bin/run.sh


ENTRYPOINT ["/usr/local/bin/run.sh"]