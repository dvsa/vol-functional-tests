@printAndSign
@CASE-DETAILS
@int_regression
@FullRegression
@localmoke
Feature: Add case details

  Background:
    Given i have a valid "goods" "standard_national" licence
    And I create a new case

  @Complaint
  Scenario: API - Creating a case with a complaint
    When I add a complaint details
    Then Complaint should be created

  @Convictions
  Scenario: API - Add a conviction to a case
    When I add conviction details
    Then Conviction should be created

  @Conditions-Undertaking
  Scenario: API - Add a condition-undertaking to a case
    When I add condition undertaking details
    Then the condition undertaking should be created

  @Submission
  Scenario: API - Add a submission
    When I add submission details
    Then the submission should be created

  @CaseNote
  Scenario: API - Add a case note
    When I add notes
    Then case notes should be created