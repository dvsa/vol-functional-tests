@OLCS-13203
@printAndSign
@int_regression
@FullRegression

Feature: Change Validation On Interim Vehicle Authority

  Background:
    Given i have a valid "goods" "standard_national" licence
    And i create an admin and url search for my licence
    And i create a variation in internal

  @reads-and-writes-system-properties
  Scenario: Interim Vehicle Authority Greater than Application Vehicle Authority
    When  I have an interim vehicle authority greater than my application vehicle authority
    Then  I should get an error when i save the application

  @reads-and-writes-system-properties
  Scenario: Interim Vehicle Authority Equal to Application Vehicle Authority
    When I have an interim vehicle authority equal to my application vehicle authority
    Then I should be able to save the application without any errors

  @reads-and-writes-system-properties
  Scenario: Interim Vehicle Authority Less than Application Vehicle Authority
    When I have an interim vehicle authority less than my application vehicle authority
    Then I should be able to save the application without any errors