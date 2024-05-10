FROM ghcr.io/dvsa/dvsa-docker-images/java/11/corretto AS base

ENV exclude_tags = "NODEFAULT"
ENV cucumberTags = "NODEFAULT"
ENV platformEnv = "NODEFAULT"
ENV platform = "NODEFAULT"
 
ENV browserName = "NODEFAULT"
ENV browserVersion = "NODEFAULT"
 
ENV gridURL = "NODEFAULT"

COPY  code .

CMD ["mvn", "--batch-mode clean verify $mavenOptions -U -Dwdm.proxy=${proxyHost}:${proxyPort} -Dhttps.proxyHost=${proxyHost} -Dhttps.proxyPort=${proxyPort} -Dhttp.proxyHost=${proxyHost} -Dhttp.proxyPort=${proxyPort} -Dhttp.nonProxyHosts=${noProxyJava} -Denv=${platformEnv} -Dbrowser=${browserName} -DbrowserVersion=${browserVersion} -Dplatform=${platform} -DgridURL=${gridURL} -Dtag.name="(not ${exclude_tags})" -Dcucumber.filter.tags="${cucumberTags}"]


