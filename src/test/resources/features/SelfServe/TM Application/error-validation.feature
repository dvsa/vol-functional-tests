Feature: Error Validation for TM Application

  Background:
    Given verify has been switched "On"
    And I am the operator and not the transport manager
    And i have a "goods" "GB" partial application

  Scenario: Error message for future TM DOBs
    When create a user and add them as a tm with a future DOB
    Then two TM DOB error should display

  Scenario: Error message for future TM DOBs (application signed in as OP)
    When i add an operator as a transport manager with a future DOB
    Then two TM DOB error should display


    # Need to check application, variation for both signed in as tm and not.
    # Automation takes you to signed in as tm.
    # Adding a new user from there sets it as not signed in as
    # that new added in user so both cases are testable in two tests.