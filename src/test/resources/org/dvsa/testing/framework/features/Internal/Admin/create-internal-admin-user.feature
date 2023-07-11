@INT-NEW-ADMIN-USER
@cross-browser
@int_regression
@FullRegression
@printAndSign
Feature: Create a new admin user

  Scenario: Creating a internal admin user
    When I create a new internal admin user
    Then I should be able to login with my new credentials

  Scenario: Creating a internal system admin user
    When I create a new system admin user
    Then I should be able to login with my new credentials