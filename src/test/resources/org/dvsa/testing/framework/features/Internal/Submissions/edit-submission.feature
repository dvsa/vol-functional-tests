@int_regression

Feature: Internal users wishes to edit a submission

  Background:
    Given i have a valid "goods" "standard_national" licence
    And I create a new case
    And i have logged in to internal

    Scenario: Internal user edits a submission
      Given I have a submission
      And I edit that submission
      Then The change should be displayed on the Submission detail page
