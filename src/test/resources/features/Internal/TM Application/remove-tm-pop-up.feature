@OLCS-19478
@INT
@LAST-TM-POP-UP
@int_regression
Feature: Remove last Transport Manager (TM) pop up

  Background:
    Given i have an application with a transport manager

  Scenario: Pop up should be displayed when last TM is removed
    Given the licence has been granted
    When the internal user goes to remove the last transport manager
    Then a pop up message should be displayed

  Scenario: Pop up should display new warning message when a self-serve user removes a TM
    Given the licence has been granted
    When a self-serve user removes the last TM
    Then the remove TM popup should not be displaying new TM remove text

  Scenario: Pop up should display new warning message when the last TM is removed from an application
    And the licence has been granted
    When the internal user goes to remove the last transport manager
    Then the remove TM popup should not be displaying new TM remove text

  Scenario: Error message should be displayed if no option is chosen
    Given the licence has been granted
    When the internal user goes to remove the last transport manager
    And user attempts to remove the last TM without selecting an option
    Then an error message should be displayed

  Scenario: Pop up should not displayed when removed from a variation
    Given the licence has been granted
    When i create a variation
    And i update the licence type
    And the internal user goes to remove the last transport manager
    Then the remove TM popup should not be displaying new TM remove text

  Scenario: Letter trigger when caseworker removes last TM on Internal
    And the licence has been granted
    When the transport manager has been removed by an internal user
    And the removal date is changed to 48 hours into the future
    And the last tm letter batch job has run
    Then the TM email should be generated and letter attached