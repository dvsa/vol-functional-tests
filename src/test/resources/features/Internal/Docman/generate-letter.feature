@INT
@Generate-Letter
@int_regression
Feature: Generate letter pop up should contain letter details

  Background:
    Given i have a valid "goods" "standard_national" licence
    And i have logged in to internal
    When i update my operating system on internal to "Windows 7"
    And i url search for my licence

  Scenario: Check generate letter pop up
    When i generate a letter
    Then The pop up should contain letter details