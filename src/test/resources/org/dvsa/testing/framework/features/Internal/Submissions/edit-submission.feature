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

      Scenario: Internal user adds comments to a submission
        Given I have a submission
        And I add a comment under the TM section
        Then that comment should be displayed

