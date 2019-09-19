@SS
@SS-ADD-DIRECTOR
@ss_regression
@apiBreak
Feature: Add a director variation

  Background:
    Given i have a valid "public" "si" licence
    And i have logged in to self serve

  Scenario: Director without any convictions
    When i add a new person
    And i enter "No" to financial details question
    And i enter "No" previous convictions details question
    Then a new director should be added to my licence
    And a non urgent task is created in internal

  Scenario: Director with convictions and bankruptcy
    When i add a new person
    And i enter "Yes" to financial details question
    And i enter "Yes" previous convictions details question
    Then a new director should be added to my licence
    And an urgent task is created in internal

  Scenario: Director with convictions and no bankruptcy
    When i add a new person
    And i enter "No" to financial details question
    And i enter "Yes" previous convictions details question
    Then a new director should be added to my licence
    And an urgent task is created in internal

  Scenario: Director with convictions check for snapshot in internal
    When a new director has been added
    Then a snapshot should be created in internal

  Scenario: Add multiple directors
    When i add a director
    And i add a new director
    Then i should have multiple directors on my application

  Scenario: No task should be created for removing person
    When i add a director
    And i remove a director
    Then a task should not be created in internal

  Scenario: Task should be created in internal when last TM is removed
    When i remove a the last director
    Then a task is created in internal