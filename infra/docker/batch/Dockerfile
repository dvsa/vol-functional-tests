FROM maven:3.9.6-amazoncorretto-11 AS builder
WORKDIR /build

COPY pom.xml .
COPY settings.xml /root/.m2/settings.xml
COPY src ./src

RUN mvn clean package -DskipTests && \
    echo "Verifying build artifacts:" && \
    ls -la target/*.jar

FROM amazoncorretto:11

ENV proxyHost=$proxyHost \
    proxyPort=$proxyPort \
    JAVA_HOME=/usr/lib/jvm/java-11-amazon-corretto \
    PATH=${JAVA_HOME}/bin:${PATH}

WORKDIR /app

COPY --from=builder /build/target/*.jar ./app.jar

RUN yum update -y && \
    yum install -y \
        zip-3.* \
        unzip-6.* \
        curl-7.* \
        python3-3.* && \
    yum clean all && \
    rm -rf /var/cache/yum && \
    python3 -m pip install --no-cache-dir awscli==1.27.165

RUN java -version && \
    aws --version && \
    echo "Verifying JAR file:" && \
    ls -la app.jar

CMD ["java", "-jar", "app.jar"]