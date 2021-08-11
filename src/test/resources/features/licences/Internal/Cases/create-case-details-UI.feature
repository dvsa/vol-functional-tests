@int_regression
@OLCS-24339

Feature: Public enquiry added and published and deletion of case notes

  Background:
    Given i have a valid "goods" "standard_national" licence
    And I create a new case
    And i have logged in to internal
    When i url search for my licence

  @inquiry
  Scenario: Publish public inquiry
    And i add a new public inquiry
    And i add and publish a hearing
    Then the public inquiry should be published

  Scenario: Delete case note
    And I add notes
    And I delete a case note
    Then the note should be deleted

  @cross-browser
  @Submission
  Scenario: Add a submission
    When i add a submission
    Then the submission details should be displayed

    #TODO: Need to do UI versions of these
  Scenario: Creating a case with a complaint

  Scenario: Add a conviction to a case

  Scenario: Add a condition-undertaking to a case

  Scenario: Add a submission

  Scenario: Add a case note