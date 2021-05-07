Feature: Sign in page

  Background:
    Given I am on the VOL self-serve site

  @OLCS-21940 @EXTERNAL
  Scenario: Displays validation error summary box
    When I sign in without inserting any credentials
    Then I should see the validation errors for sign in page