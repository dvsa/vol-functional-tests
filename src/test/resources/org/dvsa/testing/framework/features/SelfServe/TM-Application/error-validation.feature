@ss_regression
@TM-validation
@FullRegression
Feature: Error Validation for TM Application

  Background:
    And i have a "goods" "GB" partial application
    And I am the operator and not the transport manager


  Scenario: Error message for future TM DOBs (application not signed in as OP)
    When create a user and add them as a tm with a future DOB
    Then two TM DOB errors should display

  @tm-DOB
  Scenario: Error message for future TM DOBs (application signed in as OP)
    When i add an operator as a transport manager with a future DOB
    Then two TM DOB errors should display

  @hours-worked
  Scenario: Error message for TM with no hours worked
    When i add an operator as a transport manager with a no hours worked
    Then two worked hours errors should display