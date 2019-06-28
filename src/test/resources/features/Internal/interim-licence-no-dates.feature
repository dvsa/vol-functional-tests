@OLCS-17639
@INT
@INTERIM-NO-DATES
Feature: Change Date Validation On Interim Applications

  Background:
    Given i have a valid "goods" "sn" licence
    And i create and url search for my licence
    And i create a variation in internal

  Scenario: Date not required when creating an interim application
    When I create an interim application with no start and end dates
    Then I should not error when i save the application

  Scenario: Date required when granting an interim application
    When I create an interim application with a start and no end date
    Then I should error when i attempt to grant the application