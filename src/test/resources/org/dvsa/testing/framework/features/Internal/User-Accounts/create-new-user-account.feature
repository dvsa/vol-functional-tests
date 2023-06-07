@int_regression
@add-user
@FullRegression

Feature: Create a new Internal User

  Scenario: Users wishes to Login & Create a new internal user
    Given i have an admin account to add users
    And I have logged into the internal application
    When I add a new User
    Then User should be created
