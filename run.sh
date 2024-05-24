# Echo the command to be captured in logs
echo "Now running [ mvn --batch-mode clean verify $mavenOptions -U -Dwdm.proxy=${proxyHost}:${proxyPort} -Dhttps.proxyHost=${proxyHost} -Dhttps.proxyPort=${proxyPort} -Dhttp.proxyHost=${proxyHost} -Dhttp.proxyPort=${proxyPort} -Dhttp.nonProxyHosts=${noProxyJava} -Denv=${platformEnv} -Dbrowser=${browserName} -DbrowserVersion=${browserVersion} -Dplatform=${platform} -DgridURL=_hidden_ -Dtag.name=\"(not ${exclude_tags})\" -Dcucumber.filter.tags=\"${cucumberTags}\" ] .."

if [ $? -eq 0 ]; then
  mvn --batch-mode clean verify $mavenOptions -U -Dwdm.proxy=${proxyHost}:${proxyPort} -Dhttps.proxyHost=${proxyHost} -Dhttps.proxyPort=${proxyPort} -Dhttp.proxyHost=${proxyHost} -Dhttp.proxyPort=${proxyPort} -Dhttp.nonProxyHosts=${noProxyJava} -Denv=${platformEnv} -Dbrowser=${browserName} -DbrowserVersion=${browserVersion} -Dplatform=${platform} -DgridURL=${gridURL} -Dtag.name="(not ${exclude_tags})" -Dcucumber.filter.tags="${cucumberTags}"
  zip -qr allure.zip target Reports
  aws s3 cp allure.zip s3://${resultsTargetBucket}/${resultsTargetBucketPath}/${resultsBuildNumber}-${RUN-BUILDID}/
else
  echo "Maven command failed!"
fi
