@int_regression
@internal_submissions

Feature: Internal users wishes to edit a submission

  Background:
    Given i have a valid "goods" "standard_national" licence
    When I create a new case
    Then i have logged in to internal as "admin"
    And I have a submission

   @edit-submission
  Scenario: Internal user edits a submission
    And I edit that submission
    Then The change should be displayed on the Submission detail page

   @comment-submission
  Scenario: Internal user adds comments to a submission
    And I add a comment under the TM section
    Then that comment should be displayed

   @close-submission
  Scenario: Internal user closes a submission
    When I close a submission
    Then the sub status should be closed

   @attach-submission
  Scenario: Internal users attaches a file
    When I attach a file to the submission
    Then that file should be displayed


  Scenario: Check submission drop down
    Given I add and assign a Submission
    Then The drop down does not include non TC users

  Scenario: Check user is able to add comments and upload a file under every
  sub-heading
    Given I have a submission
    When I attach a file and write a comment under every sub heading
    Then the comment and file should be visible

