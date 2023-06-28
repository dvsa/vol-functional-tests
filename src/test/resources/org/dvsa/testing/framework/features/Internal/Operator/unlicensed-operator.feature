Feature: Unlicensed operator

  Background:
    Given i have an internal admin user
    And i have logged in to internal

  Scenario: Create unlicensed operator
    When i create an unlicensed operator
    Then the operator should be created
    And searchable in internal

  Scenario: Add a vehicle to unlicensed operator
    When i create an unlicensed operator
    Then i should be able to add vehicles