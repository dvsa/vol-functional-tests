@int_regression
Feature: Create a new Internal User

  Scenario: Users wishes to Login & Create a new internal user
    Given I am on the VOL internal site
    And I have logged into the internal application
    When I add a new User
    Then User should be created

