@INT
Feature: Grant publications in internal for all licence types

  Scenario: Apply for a different type of licence
    When i have logged in to internal
    And i navigate to the admin publications page
    And i generate and publish all "17" publications
