@INT-SEARCH
@int_regression
Feature: Internal Search

  Background:
    Given i have a valid "public" "standard_national" licence

  @smoketest
  Scenario: Search a Licence on Internal
    When i search for and click on my licence
    Then the "Licence details" page should display

  @smoketest
  Scenario: Search an Application on Internal
    When i search for and click on my application
    Then the "Application details" page should display

  Scenario: Search a Case on Internal
    And I create a new case
    When i search for and click on my case
    Then the "Case details" page should display

  Scenario: Search a PSV Disc on Internal
    And discs have been added to my licence
    When i search for my psv disc and click on my licence and discs
    Then the "Licence discs" page should display
    And the licence discs should be present

  Scenario: Search for Address on Internal
    When i search for my address and click on my licence and addresses
    Then the "Correspondence address" page should display
