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
- Rename org/dvsa/testing/framework/stepdefs/PSVapplication.java to Application.
- Change all passwords from config to use secrets manager.
- CreateOverview - can be moved in to util
- Before step for EBSR pre-processing to update xml
- Need to refactor out getPastDate, getPastMonth, Year, etc. All replaced for new function getPastCurrentOrFutureDate (possibly renamed) from Active Support.
- Possibly refactor getCurrentDay/Month/Year to either return strings or add new methods enterText Method to accept int input to parse into a String and run string method.
- Refactor db to not take the environment as a variable. Instead, a class to set environment variables that are then pulled into active support from the top level.
- Add hashmap for dates for clarity of what date is what. I.e. dates[0] is instead dates['day'] or something similar.
- Place variable in ESBR.xml method to stop it updating and requiring changes in git.