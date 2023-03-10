@int_regression
@public_holiday

Feature: Admin wishes to add and delete a public holiday

  Background:
    Given i have a valid "public" "standard_national" licence
    When I am on the public holidays page

  Scenario: Admin adds a public holiday
    Given an admin adds a public holiday
    Then that holiday should be displayed

  Scenario: Admin wishes to edit a public holiday
    Given an admin edits a public holiday
    Then that edited holiday should be displayed

  Scenario: Admin wishes to delete a public holiday
    Given an admin deletes a public holiday
    Then that holiday should not be displayed


