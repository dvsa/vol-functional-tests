FROM maven:3.9.6-amazoncorretto-11 AS builder
WORKDIR /build

COPY pom.xml .
COPY settings.xml /root/.m2/settings.xml
COPY src ./src
RUN mvn clean package -DskipTests

FROM amazoncorretto:11

ENV JAVA_HOME=/usr/lib/jvm/java-11-amazon-corretto \
    MAVEN_HOME=/usr/share/maven \
    PATH=${JAVA_HOME}/bin:/usr/share/maven/bin:${PATH}

WORKDIR /app

RUN yum update -y && \
    yum install -y \
        zip-3.* \
        unzip-6.* \
        curl-7.* \
        python3-3.* && \
    yum clean all && \
    rm -rf /var/cache/yum && \
    python3 -m pip install --no-cache-dir awscli==1.27.165

COPY --from=builder /usr/share/maven /usr/share/maven

RUN mkdir -p /root/.m2
COPY settings.xml /root/.m2/settings.xml
COPY --from=builder /root/.m2/repository /root/.m2/repository

RUN ls -la /usr/share/maven/bin/mvn && \
    chmod +x /usr/share/maven/bin/mvn

COPY pom.xml ./pom.xml
COPY src ./src

COPY --from=builder /build/target/*.jar ./app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]