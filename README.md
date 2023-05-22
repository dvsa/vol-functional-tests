# Functional Tests
This project holds functional tests for the VOL website. These end to end tests are facilitated through the use of WebDriver, cucumber and internal vol libraries.

## Prerequisite 
The following technologies should be installed on your system.
* Java JDK 8
* Maven 3

## Technologies
* Java
* WebDriver
* Cucumber
* Maven

## Additional build notes
You need access to the `org.dvsa.testing.lib` repository in order to build the project.  To access this, you need:
- access to the VPN
- login credentials(?) which are found (somewhere?)


## Executing

``mvn clean test -Denv= -Dbrowser= -Dcucumber.filter.tags="@xx" ``

## Reports
To produce the reports run the following command in your terminal
``mvn allure:report``

## Dependency Vulnerability Scanner
To scan use the following command ``mvn verify -DskipTests``

### Certificate issues

If maven report issues with the nvd.nist.gov certificate you can follow the steps below to add it

*  Follow the steps here https://github.com/jeremylong/InstallCert using the domain `nvd.nist.gov`. This will download and add the cert to your system keystore.
*  Add the certificate to the java keystore using one of the following commands: 
 
`JDK installed: keytool -importcert -file <path of extracted cert> -alias nvd-nist-gov -keystore $(/usr/libexec/java_home)/jre/lib/security/cacerts`

`JDK not installed: keytool -importcert -file <path of extracted cert> -alias nvd-nist-gov -keystore $(/usr/libexec/java_home)/lib/security/cacerts`

## Refactoring todos

### General
- Rename stepdefs/PSVApplication.java to application.java
- CreateOverview - can be moved in to util
- Before step for EBSR pre-processing to update xml
- Place variable in import EBSR.xml method to stop it updating and requiring changes in git.
- use gmail api to get passwords instead of getting passwords from the S3 Bucket.
- Look at Google accessibility for method to check that tab skipped to main content for skipToMainContentAndCheck function. Maybe look at using Axe.


### EUPA refactor
- Add documentation on the changes and how to modify things.