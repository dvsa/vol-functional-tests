@ss_regression
@SS-PASSWORD-RESET
@FullRegression
@printAndSign

Feature: resetting password with valid/invalid user
Background:
  Given I create a new external user

  @valid-reset  @reads-and-writes-system-properties
  Scenario: Reset password for valid user
    When i reset my password
    And I receive the reset password link via email
    Then I should be able to login with my new password

  @reads-system-properties
  Scenario: Reset password for invalid user
    Given i try resetting my password
    Then i will receive an error that username invalid

#  @WIP @WIP
#  Scenario: try reset password for inactive user
#    Given i have a valid "goods" "standard_national" licence
#    And i then try reset my password
#    Then i will receive an error for inactive account