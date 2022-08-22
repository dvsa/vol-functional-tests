@OLCS-19478
@LAST-TM-POP-UP

Feature: Remove last Transport Manager (TM) pop up

  Background:
    Given i have an application with a transport manager

  @int_regression
  Scenario: Last TM is removed displays a pop up
    Given the licence has been granted
    When the internal user goes to remove the last transport manager
    Then a pop up message should be displayed

  @int_regression
  Scenario: Self-serve user removes a TM displays warning pop up
    Given the licence has been granted
    When a self-serve user removes the last TM
    Then the remove TM popup should not be displaying new TM remove text

  @int_regression
  Scenario: Application last TM is removed displays a pop up
    And the licence has been granted
    When the internal user goes to remove the last transport manager
    Then the remove TM popup should not be displaying new TM remove text

  @int_regression
  Scenario: Error message should be displayed if no option is chosen
    Given the licence has been granted
    When the internal user goes to remove the last transport manager
    And user attempts to remove the last TM without selecting an option
    Then an error message should be displayed

  @int_regression
  Scenario: Pop up should not displayed when removed from a variation
    Given the licence has been granted
    When i create a variation
    And i update the licence type
    And the internal user goes to remove the last transport manager
    Then the remove TM popup should not be displaying new TM remove text

  @Jenkins @deprecated
  Scenario: Letter trigger when caseworker removes last TM on Internal
    And the licence has been granted
    When the transport manager has been removed by an internal user
    And the removal date is changed to 48 hours into the future
    And the last tm letter batch job has run
    Then the TM email should be generated and letter attached