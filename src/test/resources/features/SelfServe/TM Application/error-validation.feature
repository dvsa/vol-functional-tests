Feature: Error Validation for TM Application

  Background:
    And I am the operator and not the transport manager
    And i have a "goods" "GB" partial application

  Scenario: Error message for future TM DOBs (application not signed in as OP)
    When create a user and add them as a tm with a future DOB
    Then two TM DOB errors should display

  Scenario: Error message for future TM DOBs (application signed in as OP)
    When i add an operator as a transport manager with a future DOB
    Then two TM DOB errors should display

  Scenario: Error message for TM with no hours worked
    When i add an operator as a transport manager with a no hours worked
    Then two worked hours errors should display