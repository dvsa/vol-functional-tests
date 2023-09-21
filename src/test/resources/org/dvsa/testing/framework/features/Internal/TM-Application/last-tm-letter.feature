Feature: Last TM letter sent

  Background:
    Given i have an application with a transport manager

    Scenario: Last TM removed and letter sent
      When the licence has been granted
      And the internal user goes to remove the last transport manager
      And the user confirms a letter should be issued
      And the last TM letter job is run
      Then the TM email should be generated and letter attached
