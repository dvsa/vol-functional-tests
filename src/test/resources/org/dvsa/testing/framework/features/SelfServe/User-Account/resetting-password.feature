@ss_regression
@SS-PASSWORD-RESET
Feature: resetting password with valid/invalid user
Background:
  Given I create a new external user

  Scenario: Reset password for valid user
    And i reset my password
    And I receive the reset password link via email

  Scenario: Reset password for invalid user
    Given i try resetting my password
    Then i will receive an error that username invalid

#  @WIP
#  Scenario: try reset password for inactive user
#    Given i have a valid "goods" "standard_national" licence
#    And i then try reset my password
#    Then i will receive an error for inactive account