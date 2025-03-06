FROM maven:3.9.6-amazoncorretto-17 as maven
ENV platform="LINUX"
ENV gridURL="https://selenium-hub.olcs.dev-dvsacloud.uk/"
ENV proxyHost=$proxyHost
ENV proxyPort=$proxyPort
WORKDIR /app

# Install system dependencies and AWS CLI in a single layer
RUN yum update -y && \
    yum install -y zip-3.* unzip-6.* && \
    yum clean all && \
    rm -rf /var/cache/yum && \
    curl "https://awscli.amazonaws.com/awscli-exe-linux-x86_64.zip" -o "awscliv2.zip" && \
    unzip awscliv2.zip && \
    ./aws/install --bin-dir /usr/local/bin --install-dir /usr/local/aws-cli --update && \
    rm -f awscliv2.zip

# Copy Maven settings
COPY ./settings.xml /root/.m2/settings.xml

# Copy pom files for dependency resolution
COPY ./pom.xml ./

# Download all dependencies and prepare the app in a single layer
RUN mvn dependency:go-offline -B && \
    chmod +x ./run.sh

# Now copy the rest of the application
COPY . .

CMD ["/bin/sh", "-c", "./run.sh"]