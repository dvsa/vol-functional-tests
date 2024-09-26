#!/bin/bash

# Description: This script is used to run the tests in the docker container

# In environments some values are passed as environment variables to the container from the job definition
# they are set in the job definition others are set in the Dockerfile at build time or passed to
# the container at runtime via batch

# full list of environment variables passed to the container
# - platformEnv
# - browserName
# - browserVersion
# - platform
# - gridURL
# - exclude_tags
# - cucumberTags
# - resultsTargetBucket
# - resultsTargetBucketPath
# - resultsBuildNumber
# - RUN-BUILDID
# - proxyHost
# - proxyPort
# - noProxyJava
# - mavenOptions


# check if all the environment variables are set
check_environment_variables() {
  # List of required environment variables
  local required_vars=(
    "platformEnv"
    "browserName"
    "browserVersion"
    "platform"
    "gridURL"
    "exclude_tags"
    "cucumberTags"
    "resultsTargetBucket"
    "resultsTargetBucketPath"
    "buildId"
    "proxyHost"
    "proxyPort"
  )

  # Check if any of the required variables are not set
  for var in "${required_vars[@]}"; do
    if [[ -z "${!var}" ]]; then
      echo "Error: Environment variable '$var' is not set."
      return 1
    fi
  done

  return 0
}

check_environment_variables || exit 1


if [ ! -f "/app/test-artifact.jar" ]; then
  # Echo the command to be captured in logs
  echo "Now running [ mvn --batch-mode clean verify $mavenOptions -U -Dwdm.proxy=${proxyHost}:${proxyPort} -Dhttps.proxyHost=${proxyHost} -Dhttps.proxyPort=${proxyPort} -Dhttp.proxyHost=${proxyHost} -Dhttp.proxyPort=${proxyPort} -Dhttp.nonProxyHosts=${noProxyJava} -Denv=${platformEnv} -Dbrowser=${browserName} -DbrowserVersion=${browserVersion} -Dplatform=${platform} -DgridURL=_hidden_ -Dtag.name=\"(not ${exclude_tags})\" -Dcucumber.filter.tags=${cucumberTags} ] .."

  if [ -z "${mavenOptions}" ]; then
    mvn --batch-mode clean verify  $mavenOptions -fae  -Dwdm.proxy=${proxyHost}:${proxyPort} -Dhttps.proxyHost=${proxyHost} -Dhttps.proxyPort=${proxyPort} -Dhttp.proxyHost=${proxyHost} -Dhttp.proxyPort=${proxyPort} -Dhttp.nonProxyHosts=${noProxyJava} -Denv=${platformEnv} -Dbrowser=${browserName} -DbrowserVersion=${browserVersion} -Dplatform=${platform} -DgridURL=${gridURL} -Dtag.name="(not ${exclude_tags})" -Dcucumber.filter.tags=${cucumberTags}
  else
    mvn --batch-mode clean verify -fae  -Dwdm.proxy=${proxyHost}:${proxyPort} -Dhttps.proxyHost=${proxyHost} -Dhttps.proxyPort=${proxyPort} -Dhttp.proxyHost=${proxyHost} -Dhttp.proxyPort=${proxyPort} -Dhttp.nonProxyHosts=${noProxyJava} -Denv=${platformEnv} -Dbrowser=${browserName} -DbrowserVersion=${browserVersion} -Dplatform=${platform} -DgridURL=${gridURL} -Dtag.name="(not ${exclude_tags})" -Dcucumber.filter.tags=${cucumberTags} -Dcucumber.options="-- io.qameta.allure.cucumber7jvm.AllureCucumber7Jvm"
  fi

  mvn allure:report
  # create the report zip file
  zip -qr allure.zip target
  cd target
  aws s3 cp site s3://${resultsTargetBucket}/${resultsTargetBucketPath}/${buildId}/site/ --recursive
  aws s3 cp ../allure.zip s3://${resultsTargetBucket}/${resultsTargetBucketPath}/${buildId}/
else
  echo "Using existing test-artifact.jar found in /app."
fi

# Run the JAR file
echo "Now running [ java -jar /app/test-artifact.jar ] .."
java -jar /app/test-artifact.jar
if [ $? -ne 0 ]; then
  echo "Error: The Java application exited with status $?"
  exit 1
else
  echo "Java application executed successfully."
fi