@int_regression
@internal_submissions

Feature: Internal users wishes to edit a submission

  Background:
    Given i have a valid "goods" "standard_national" licence
    When I create a new case
    Then i have logged in to internal
    And I have a submission

  Scenario: Internal user edits a submission
    And I edit that submission
    Then The change should be displayed on the Submission detail page

  Scenario: Internal user adds comments to a submission
    And I add a comment under the TM section
    Then that comment should be displayed

  Scenario: Internal user closes a submission
    When I close a submission
    Then the sub status should be closed

  Scenario: Internal users attaches a file
    When I attach a file to the submission
    Then that file should be displayed




