@int_regression @unlicensed_operator
Feature: Unlicensed operator

  Background:
    Given i have an internal admin user

  @unlicensed_operator_add @smoke @localsmoke @smoke
  Scenario: Create unlicensed operator
    When i create an unlicensed operator
    Then the operator should be created

  @unlicensed_operator_vehicle
  Scenario: Add a vehicle to unlicensed operator
    When i create an unlicensed operator
    Then i should be able to add vehicles
    And the details should be displayed in the vehicles table