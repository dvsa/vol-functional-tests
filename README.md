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


## Executing
``mvn clean verify -Denv= -Dbrowser= ``

On Environments that do not support verify run the follwing command 

``mvn clean install -Denv=da -Dbrowser=chrome -DdbUsername=olcsapi -DdbPassword ""  -Dcucumber.options="--tags @ss_regression --tags ~@gov-verify" ``

  
The environment and browser properties need to be set otherwise the test will not execute. If only environment and browser properties are set, the harness will
create by default ``a GB goods standard national limited company licence``

``@PSV-LAST-TM-TRIGGER`` test requires access to the Jenkins api and database, so for this test the following properties 
``-DJENKINS_USERNAME= -DJENKINS_PASSWORD= -DdbUsername= -DdbPassword=`` need to be set

## Executing 

### Including tags
``mvn clean verify {system props e.g. -Denv -Dbrowser} -Dcucumber.options="--tags @tag``

### Excluding tags
``mvn clean verify {system props e.g. -Denv -Dbrowser} -Dtag.name='(not @tag)' -Dcucumber.options="--tags @tag``

``The following system property -Dtag.name accepts a single tag 'not @tag' or multiple tags '(not @tag and not @tag)'``

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
- Rename stepdefs/PSVApplication.java to application.java
- CreateOverview - can be moved in to util
- Before step for EBSR pre-processing to update xml
- Need a waitAndClickByLinkText method. Replace in navigateThroughApplication() method.
- Place variable in ESBR.xml method to stop it updating and requiring changes in git.
- use gmail api to get passwords instead of getting passwords from the S3 Bucket.
- Look at Google accessibility for method to check that tab skipped to main content for skipToMainContentAndCheck function. Maybe look at using Axe.
- Separate UIJourneySteps into a few smaller classes for specific methods. I.e. Search methods.
- Create new methods for true and false statements. I.e. world.createLicence.getLicenceType().equals("restricted") -> isLicenceRestricted().
- Fix page action dependencies.
- Add Internal class for internal methods so the methods don't require 'in Internal' in the name.
- Get rid of si and sn and refactor to be able to create all licence types. Check what extra steps/ not needed steps for different licence types.
