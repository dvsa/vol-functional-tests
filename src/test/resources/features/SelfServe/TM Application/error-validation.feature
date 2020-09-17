Feature: Error Validation for TM Application

  Background:
    Given verify has been switched "On"
    And I am the operator and not the transport manager
    And i have a "goods" "GB" partial application

  Scenario: Error message for future TM DOBs (application not signed in as OP)
    When create a user and add them as a tm with a future DOB
    Then two TM DOB error should display

  Scenario: Error message for future TM DOBs (application signed in as OP)
    When i add an operator as a transport manager with a future DOB
    Then two TM DOB error should display