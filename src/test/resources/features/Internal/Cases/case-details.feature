@INT
@CASE-DETAILS
@int_regression
Feature: Add case details

  Background:
    Given i have a valid "goods" "standard_national" licence
    And I create a new case

  @Complaint
  Scenario: Creating a case with a complaint
    When I add a complaint details
    Then Complaint should be created

  @Convictions
  Scenario: Add a conviction to a case
    When I add conviction details
    Then Conviction should be created

  @Conditions-Undertaking
  Scenario: Add a condition-undertaking to a case
    When I add condition undertaking details
    Then the condition undertaking should be created

  @Submission
  Scenario: Add a submission
    When I add submission details
    Then the submission should be created

  @CaseNote
  Scenario: Add a case note
    When I add notes
    Then case notes should be created