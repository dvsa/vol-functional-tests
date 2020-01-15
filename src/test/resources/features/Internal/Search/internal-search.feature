@INT-SEARCH
Feature: Internal Search


  Background:
    Given i have a valid "public" "standard_national" licence

  Scenario: Search a Licence on Internal
    When i search for and click on my licence
    Then the "Licence details" page should display

  Scenario: Search an Application on Internal
    When i search for and click on my application
    Then the "Application details" page should display

  Scenario: Search a Case on Internal
    And I create a new case
    When i search for and click on my case
    Then the "Case details" page should display

  Scenario: Search a PSV Disc
    And discs have been added to my licence