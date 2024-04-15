@INT-SEARCH
@FullRegression
@printAndSign
Feature: Internal Search

  Background:
    Given i have a valid "public" "standard_national" licence
    And i have logged in to internal as "admin"

  @smoketest @searchLicence @int_regression
  Scenario: Search a Licence on Internal
    When i search for and click on my licence "OB1134621"
    Then the "Licence details" page should display

  @smoketest @searchApplication @int_regression
  Scenario: Search an Application on Internal
    When i search for and click on my application
    Then the "Application details" page should display

  @searchCase @int_regression
  Scenario: Search a Case on Internal
    And I create a new case
    When i search for and click on my case
    Then the "Case details" page should display

  @psv-disc
  Scenario: Search a PSV Disc on Internal
    And discs have been added to my licence
    When i search for my psv disc
    And the licence discs should be present

  @searchAddress
  Scenario: Search for Address on Internal
    When i search for my address and click on my licence and addresses
    Then the "Correspondence address" page should display

  @searchVehicle @int_regression
  Scenario: Search for Vehicle on Internal
    When i search for a vehicle by registration "AE83XUF"
    Then the registration "AE83XUF" should be displayed