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

# Check if platformEnv is "int" and replace with "qa"
if [ "${platformEnv}" = "int" ]; then
    echo "Replacing platformEnv 'int' with 'qa'"
    platformEnv="qa"
fi


echo "Now running [ mvn --batch-mode clean verify $mavenOptions ... ] .."

if [ -z "${mavenOptions}" ]; then
  mvn --batch-mode clean verify  $mavenOptions -fae -U -Dwdm.proxy=${proxyHost}:${proxyPort} -Dhttps.proxyHost=${proxyHost} -Dhttps.proxyPort=${proxyPort} -Dhttp.proxyHost=${proxyHost} -Dhttp.proxyPort=${proxyPort} -Dhttp.nonProxyHosts=${noProxyJava} -Denv=${platformEnv} -Dbrowser=${browserName} -DbrowserVersion=${browserVersion} -Dplatform=${platform} -DgridURL=${gridURL} -Dtag.name="(not ${exclude_tags})" -Dcucumber.filter.tags=${cucumberTags}
else
  mvn --batch-mode clean verify -fae -U -Dwdm.proxy=${proxyHost}:${proxyPort} -Dhttps.proxyHost=${proxyHost} -Dhttps.proxyPort=${proxyPort} -Dhttp.proxyHost=${proxyHost} -Dhttp.proxyPort=${proxyPort} -Dhttp.nonProxyHosts=${noProxyJava} -Denv=${platformEnv} -Dbrowser=${browserName} -DbrowserVersion=${browserVersion} -Dplatform=${platform} -DgridURL=${gridURL} -Dtag.name="(not ${exclude_tags})" -Dcucumber.filter.tags=${cucumberTags} -Dcucumber.options="-- io.qameta.allure.cucumber7jvm.AllureCucumber7Jvm"
fi

mkdir -p target/allure-results-merged

if [ -d target/allure-results ]; then
  cp -r target/allure-results/* target/allure-results-merged/
fi

if [ -d target/allure-results-rerun ]; then
  cp -r target/allure-results-rerun/* target/allure-results-merged/
fi

allure generate target/allure-results-merged --clean -o target/allure-report

mvn allure:report
mv allure.zip ./allure_attempt_${resultsBuildNumber}.zip
zip -qr ./allure_attempt_${resultsBuildNumber}.zip target
cd target
aws s3 cp site s3://${resultsTargetBucket}/${resultsTargetBucketPath}/${buildId}/site/ --recursive
aws s3 cp ../allure_attempt_${resultsBuildNumber}.zip s3://${resultsTargetBucket}/${resultsTargetBucketPath}/${buildId}/