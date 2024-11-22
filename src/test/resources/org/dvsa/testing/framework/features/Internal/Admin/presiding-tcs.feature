@int_regression
@transportCommissionerSubmission
@FullRegression

Feature: Admin wishes to add and delete a Presiding tc

  Scenario Outline: UI - New Transport Commissioner added to a submission
    Given as a "<user_type>" I have a valid "goods" "standard_national" licence
    And I create a new case
    And i have logged in to internal as "admin"
    When i url search for my licence
    Given I am on the Presiding TC page
    When I add a Presiding TC and then create a Submission
    Then I can view the added Presiding TC in the drop down list
    Examples:
      | user_type  |
      | consultant |
      | admin      |