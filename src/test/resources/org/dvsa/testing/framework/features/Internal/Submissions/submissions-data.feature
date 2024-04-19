@int_regression
@internal_submissions
@FullRegression
  Feature: Assign submission drop down

    Background:
      Given i have a valid "goods" "standard_national" licence
      When I create a new case
      Then i have logged in to internal as "admin"

      @assign-submission
      Scenario: Check TC/DC Drop down does not contain current logged in user
      Given I add and assign a Submission
      Then The TC/DC drop down list does not contain the current user

