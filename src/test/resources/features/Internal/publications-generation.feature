@INT
@int_regression
Feature: Grant publications in internal for all licence types

  Scenario: Generate and publish Publications for different licence types
    When i have logged in to internal
    And i navigate to the admin publications page
    And i generate and publish all "17" publications