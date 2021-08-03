@ECMTREMOVALINTERNAL
Feature: Caseworker creates an ECMT removal application

  Background:
    Given I have a "goods" "standard_international" licence
    And I am on the VOL internal site
    And i create an admin and url search for my licence

    @OLCS-28232
  Scenario: Case Worker submits and pays for ECMT removal Application by Card
    And the case worker apply for an ECMT Removal application
    And I pay fee for the ECMT removal application
    Then the application goes to valid status

  @OLCS-28232
  Scenario: Case Worker submits and pays for ECMT removal Application by waiving the fee
    And the case worker apply for an ECMT Removal application
    And all fees have been waived
    Then the application goes to valid status

