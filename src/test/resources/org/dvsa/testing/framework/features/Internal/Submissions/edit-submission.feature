@int_regression
@internal_submissions

Feature: Internal users wishes to edit a submission

  Background:
    Given i have a valid "goods" "standard_national" licence
    When I create a new case
    Then i have logged in to internal
    And I have a submission

  @reads-and-writes-system-properties @edit-submission
  Scenario: Internal user edits a submission
    And I edit that submission
    Then The change should be displayed on the Submission detail page

  @reads-and-writes-system-properties @comment-submission
  Scenario: Internal user adds comments to a submission
    And I add a comment under the TM section
    Then that comment should be displayed

  @reads-and-writes-system-properties @close-submission
  Scenario: Internal user closes a submission
    When I close a submission
    Then the sub status should be closed

  @reads-and-writes-system-properties @attach-submission
  Scenario: Internal users attaches a file
    When I attach a file to the submission
    Then that file should be displayed




