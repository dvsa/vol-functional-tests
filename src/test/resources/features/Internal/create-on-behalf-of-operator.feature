@INT
@OLCS-24241
@Create-On-Behalf-Of-User
@int_regression
Feature: Internal User should be able to create an operator account

  Background:
    Given I have partially applied for a "public" "standard_national" licence

    Scenario: Caseworker submits application
      When the caseworker completes and submits the application
      And grants the application
      Then the licence is granted in Internal
      And the "PSV Licence" document is produced automatically