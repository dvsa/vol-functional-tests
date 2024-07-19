@INT-SEARCH
@FullRegression
@printAndSign
@api
Feature: Internal Search

  @smoketest @searchLicence @int_regression
  Scenario: Search a Licence on Internal
    Given I have a "goods" "restricted" application
    And I log in as an internal user with admin privileges
    When i search for and click on my licence "OB1134621"
    Then the "Licence details" page should display

  @smoketest @searchApplication @int_regression
  Scenario: Search an Application on Internal
    Given I have a "goods" "restricted" application
    When i search for and click on my application
    Then the "Application details" page should display

  @searchCase @int_regression
  Scenario: Search a Case on Internal
    Given I have a "goods" "restricted" licence
    And I create a new case
    When i search for and click on my case
    Then the "Case details" page should display

  @psv-disc
  Scenario: Search a PSV Disc on Internal
    Given I have a "Public" "restricted" licence
    And i have logged in to internal as "admin"
    And discs have been added to my licence
    When i search for my psv disc
    And the licence discs should be present

  @searchAddress
  Scenario: Search for Address on Internal
    Given I have a "goods" "restricted" licence
    When i search for my address and click on my licence and addresses
    Then the "Correspondence address" page should display

  @searchVehicle
  Scenario: Search for Vehicle on Internal
    Given I have a "goods" "restricted" application
    And i select and search for a vehicle by registration
    Then the registered VRM should display