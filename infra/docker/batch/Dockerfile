FROM maven:3.9.6-amazoncorretto-11 as builder
WORKDIR /build
COPY pom.xml .
COPY settings.xml /root/.m2/settings.xml
COPY src ./src
RUN mvn clean package -DskipTests

ENV proxyHost=$proxyHost
ENV proxyPort=$proxyPort
ENV platform="LINUX"
ENV gridURL="https://selenium-hub.olcs.dev-dvsacloud.uk/"

WORKDIR /app
COPY --from=builder /build/target/vol-functional-tests-3.3.1-SNAPSHOT.jar ./app.jar
COPY --from=builder /usr/share/maven /usr/share/maven
COPY --from=builder /root/.m2 /root/.m2
COPY --from=builder /build/pom.xml ./pom.xml
COPY --from=builder /build/src ./src

RUN yum update -y && \
    yum install -y java-11-amazon-corretto-headless zip unzip curl python3 python3-pip && \
    yum clean all && \
    rm -rf /var/cache/yum

RUN pip3 install --no-cache-dir awscli

# Set up environment variables
ENV JAVA_HOME=/usr/lib/jvm/java-11-amazon-corretto
ENV MAVEN_HOME=/usr/share/maven
ENV PATH=${JAVA_HOME}/bin:${MAVEN_HOME}/bin:${PATH}


RUN java -version && mvn -version && aws --version

CMD ["java", "-cp", "app.jar", "AppJarRunner"]