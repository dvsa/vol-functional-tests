@SS
@SS-ADD-DIRECTOR
@ss_regression
Feature: Add a director variation

  Background:
    Given i have a valid "public" "standard_international" licence
    And i have logged in to self serve

  Scenario: Director without any convictions
    When i begin adding a new director and their details
    And i enter "No" to financial details question
    And i enter "No" to previous convictions details question
    Then a new director should be added to my licence
    And a non urgent task is created in internal

  Scenario: Director with convictions and bankruptcy
    When i begin adding a new director and their details
    And i enter "Yes" to financial details question
    And i enter "Yes" to previous convictions details question
    Then a new director should be added to my licence
    And an urgent task is created in internal

  Scenario: Director with convictions and no bankruptcy
    When i begin adding a new director and their details
    And i enter "No" to financial details question
    And i enter "Yes" to previous convictions details question
    Then a new director should be added to my licence
    And an urgent task is created in internal

  Scenario: Director with convictions check for snapshot in internal
    When i add a director
    Then a snapshot should be created in internal

  Scenario: Add multiple directors
    When i add a director
    And i add another new director
    Then i should have multiple directors on my application

  Scenario: No task should be created for removing person
    When i add a director
    And i remove a director
    Then a task should not be created in internal

  Scenario: Task should be created in internal when last director is removed
    When i remove the last director
    Then a task is created in internal