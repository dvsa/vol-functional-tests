@ss_regression
@SS-PASSWORD-RESET
Feature: resetting password with valid/invalid user

  Scenario: Reset password for valid user
    Given I create a new external user
    And i reset my password
    And I receive the reset password link via email

  Scenario: Reset password for invalid user
    Given i try resetting my password
    Then i will receive an error that username invalid

  Scenario: Attempt to reset password for inactive user
    Given I create a new external user
    And i then try reset my password
    Then i will receive an error for inactive account