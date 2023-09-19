@int_regression

Feature: Admin wishes to add and delete a Presiding tc

  Background:
    Given i have a valid "goods" "standard_national" licence
    And I create a new case
    And i have logged in to internal
    When i url search for my licence

  Scenario: UI - New Transport Commissioner added to a submission
    Given I am on the Presiding TC page
    When I add a Presiding TC and then create a Submission
    Then I can view the added Presiding TC in the drop down list




