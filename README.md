# Functional Tests
This project holds functional tests for the VOL website. These end to end tests are facilitated through the use of WebDriver, cucumber and internal vol libraries.

## Prerequisite 
The following technologies should be installed on your system.
* Java JDK 17
* Maven 3.

## Technologies
* Java
* WebDriver
* Cucumber
* Maven


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

- Upgrade to Java 17
- Refactor Java 11 Methods to Java 17

##EUPA Refactor
- Add documentation on the changes and how to modify things
