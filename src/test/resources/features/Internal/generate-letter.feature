@INT
@Generate-Letter
@int_regression
Feature: Generate letter pop up should contain letter details

  Background:
    Given i have a valid "goods" "sn" licence
    And i have logged in to internal
    And i search for my licence

  Scenario: Check generate letter pop up
    When I generate a letter
    Then The pop up should contain letter details