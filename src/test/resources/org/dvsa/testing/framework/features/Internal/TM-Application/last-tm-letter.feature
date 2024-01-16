@LAST-TM-LETTER
@int_regression
@FullRegression

Feature: Last TM letter sent

  Background:
    Given i have an application with a transport manager

    Scenario: Last TM removed and letter sent
      When the licence has been granted
      And the internal user goes to remove the last transport manager
      And the user confirms a letter should be issued
      Then the last TM letter job is run
      And the last TM letter should be sent
