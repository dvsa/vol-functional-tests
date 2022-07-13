@SS
@SS-ADD-DIRECTOR
@ss_regression
Feature: Add a director variation

  Background:
    Given i have a valid "public" "standard_international" licence
    And i navigate to the "licence" directors page

  Scenario: Director without any convictions
    When I begin adding a new director and their details
    And i enter "No" to financial details question
    And i enter "N" to licence history question
    And i enter "No" to previous convictions details question
    Then a new director should be added to my licence
    And a non urgent task is created in internal

  Scenario: Director with convictions and bankruptcy
    When I begin adding a new director and their details
    And i enter "Yes" to financial details question
    And i enter "N" to licence history question
    And i enter "Yes" to previous convictions details question
    Then a new director should be added to my licence
    And an urgent task is created in internal

  Scenario: Director with convictions and no bankruptcy
    When I begin adding a new director and their details
    And i enter "No" to financial details question
    And i enter "N" to licence history question
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
    Then the last director deleted task is created in internal

  Scenario: Add a director page validation (empty fields)
    When I begin adding a director but submit empty fields
    Then the add a director page empty field validation should appear

  Scenario: Add a director page validation (incorrect information)
    When I wrongly fill in and submit the add a director page
    Then the add a director page incorrect value validation should appear

  Scenario: Director financial history page validation
    And I begin adding a new director and their details
    When I submit the empty page
    Then the director financial history page empty field validation should appear

  Scenario: Director convictions and penalties page validation
    When I begin adding a new director and their details
    And i enter "No" to financial details question
    And i enter "N" to licence history question
    When I submit the empty page
    Then the director convictions and penalties page empty field validation should appear