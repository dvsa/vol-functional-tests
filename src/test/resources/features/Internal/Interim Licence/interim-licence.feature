@OLCS-13203
@INT
@INTERIM
@int_regression
Feature: Change Validation On Interim Vehicle Authority

  Background:
    Given i have a valid "goods" "sn" licence
    And i create an admin and url search for my licence
    And i create a variation in internal

  Scenario: Interim Vehicle Authority Greater than Application Vehicle Authority
    When  I have an interim vehicle authority greater than my application vehicle authority
    Then  I should get an error when i save the application

  Scenario: Interim Vehicle Authority Equal to Application Vehicle Authority
    When I have an interim vehicle authority equal to my application vehicle authority
    Then I should be able to save the application without any errors

  Scenario: Interim Vehicle Authority Less than Application Vehicle Authority
    When I have an interim vehicle authority less than my application vehicle authority
    Then I should be able to save the application without any errors