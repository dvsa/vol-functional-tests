@OLCS-19478
@LAST-TM-POP-UP
@int_regression
@FullRegression
@printAndSign

Feature: Remove last Transport Manager (TM) pop up

  Background:
    Given i have an application with a transport manager

  @last-tm-internal  @reads-and-writes-system-properties
  Scenario: Last TM is removed displays a pop up
    Given the licence has been granted
    When the internal user goes to remove the last transport manager
    Then a pop up message should be displayed

  @reads-system-properties
  Scenario: Self-serve user removes a TM displays warning pop up
    Given the licence has been granted
    When a self-serve user removes the last TM
    Then the remove TM popup should not be displaying new TM remove text

  @reads-system-properties
  Scenario: Application last TM is removed displays a pop up
    And the licence has been granted
    When the internal user goes to remove the last transport manager
    Then the remove TM popup should not be displaying new TM remove text

  @reads-system-properties
  Scenario: Error message should be displayed if no option is chosen
    Given the licence has been granted
    When the internal user goes to remove the last transport manager
    And user attempts to remove the last TM without selecting an option
    Then an error message should be displayed

  @reads-system-properties
  Scenario: Pop up should not displayed when removed from a variation
    Given the licence has been granted
    When i create a variation
    And i update the licence type
    And the internal user goes to remove the last transport manager
    Then the remove TM popup should not be displaying new TM remove text