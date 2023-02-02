@ss_regression
@SS-PASSWORD-RESET
Feature: resetting password with valid/invalid user

  Scenario: reset password for valid user
    Given I create a new external user
    And i reset my password
    Then i will receive a message to say my password has changed

  Scenario: reset password for invalid user
    Given i try resetting my password
    Then i will receive an error that username invalid

  Scenario: try reset password for inactive user
    Given I create a new external user
    And i then try reset my password
    Then i will receive an error for inactive account