@int_regression
@your_account
@FullRegression
@printAndSign

Feature: User edits their account details

  Background:
    Given i have a valid "goods" "standard_national" licence
    When I am on the Your Account page

  @reads-and-writes-system-properties
  Scenario: User wishes to change their team
    Given I change my team
    Then my new team should be visible

  @reads-and-writes-system-properties
  Scenario: User wishes to edit their personal details
    Given I edit my personal details
    Then my details should have updated