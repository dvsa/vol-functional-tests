FROM maven:3.9.6-amazoncorretto-21 as maven
ENV platform="LINUX"
ENV gridURL="https://selenium-hub.olcs.dev-dvsacloud.uk/"

WORKDIR /app

# Install system dependencies and AWS CLI
RUN yum update -y && \
    yum install -y zip-3.* unzip-6.* && \
    yum clean all && \
    rm -rf /var/cache/yum && \
    curl "https://awscli.amazonaws.com/awscli-exe-linux-aarch64.zip" -o "awscliv2.zip" && \
    unzip awscliv2.zip && \
    ./aws/install --bin-dir /usr/local/bin --install-dir /usr/local/aws-cli --update && \
    rm -f awscliv2.zip

# Copy build-time Maven settings (without proxy)
COPY ./settings-build.xml /root/.m2/settings.xml

# Copy pom files for dependency resolution
COPY ./pom.xml ./

# Download dependencies (should work without proxy)
RUN echo "Install mvn deps" && \
    timeout 20m mvn dependency:resolve -B -Dmaven.test.skip=true -Dmaven.main.skip=true || true && \
    echo "catch for occasional stalling on first mvn run" && \
    timeout 20m mvn dependency:resolve -B -Dmaven.test.skip=true -Dmaven.main.skip=true || echo "Dependency resolution completed"

# Now copy the runtime settings file (with proxy) for later use
COPY ./settings-runtime.xml /root/.m2/settings.xml

# Copy the rest of the application
COPY . .

# Make run.sh executable
RUN chmod +x ./run.sh

CMD ["/bin/sh", "-c", "./run.sh"]