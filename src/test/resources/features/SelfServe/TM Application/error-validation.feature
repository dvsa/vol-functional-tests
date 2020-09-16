Feature: Error Validation for TM Application

  Background:
    Given verify has been switched "On"
    And I am the operator and not the transport manager
    And i have a "goods" "GB" partial application

  Scenario: Error message for future TM DOBs
    When create a user and add them as a tm with a future DOB
    Then a TM DOB error should display
