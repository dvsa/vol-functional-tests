# FROM ghcr.io/dvsa/dvsa-docker-images/java/11/corretto AS base

FROM maven:3.9.6-amazoncorretto-11 as maven

# mvn clean verify -U -Denv=qa -Dbrowser=edge -DbrowserVersion=103 -Dplatform=LINUX -DgridURL=https://selenium-hub.olcs.dev-dvsacloud.uk/ '-Dtag.name=(not *)' -Dcucumber.filter.tags=@smoketest -Dwdm.proxy=proxy.ci.olcs.dev-dvsacloud.uk:3128 -Dhttps.proxyHost=proxy.ci.olcs.dev-dvsacloud.uk -Dhttps.proxyPort=3128 -Dhttp.proxyHost=proxy.ci.olcs.dev-dvsacloud.uk -Dhttp.proxyPort=3128 '-Dhttp.nonProxyHosts=172.17/12|localhost|127.0.0.1|*.olcs.dev-dvsacloud.uk'

ENV proxyHost=
ENV proxyPort=
ENV noProxyJava=
ENV platformEnv=
ENV browserName=
ENV browserVersion=
ENV platform=
ENV gridURL=https://selenium-hub.olcs.dev-dvsacloud.uk/
ENV exclude_tags=
ENV cucumberTags=




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

COPY ./settings.xml ~/.m2/settings.xml

COPY . .

CMD ["/bin/sh", "-c", "./run.sh"]