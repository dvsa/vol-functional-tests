@SS-ADD-DIRECTOR @ss_regression @FullRegression @printAndSign
Feature: Add a director variation

  Background:
    Given i have a valid "public" "standard_international" licence
    And i navigate to the "licence" directors page

  @director-no-convictions
  Scenario: Director without any convictions
    When I begin adding a new director and their details
    And i enter "No" to financial details question
    And i enter "N" to licence history question
    And i enter "No" to previous convictions details question
    Then a new director should be added to my licence
    And a non urgent task is created in internal

  @director-convictions
  Scenario: Director with convictions and bankruptcy
    When I begin adding a new director and their details
    And i enter "Yes" to financial details question
    And i enter "N" to licence history question
    And i enter "Yes" to previous convictions details question
    Then a new director should be added to my licence
    And an urgent task is created in internal

  @director-convictions-no-bankruptcy
  Scenario: Director with convictions and no bankruptcy
    When I begin adding a new director and their details
    And i enter "No" to financial details question
    And i enter "N" to licence history question
    And i enter "Yes" to previous convictions details question
    Then a new director should be added to my licence
    And an urgent task is created in internal

  @director-convictions-snapshot
  Scenario: Director with convictions check for snapshot in internal
    When i add a director
    Then a snapshot should be created in internal

  @multiple-directors
  Scenario: Add multiple directors
    When i add a director
    And i add another new director
    Then i should have multiple directors on my application

  @directors-no-task
  Scenario: No task should be created for removing person
    When i add a director
    And i remove a director
    Then a task should not be created in internal

  @directors-task
  Scenario: Task should be created in internal when last director is removed
    When i remove the last director
    Then the last director deleted task is created in internal

  @directors-validations-1
  Scenario: Add a director page validation (empty fields)
    When I begin adding a director but submit empty fields
    Then the add a director page empty field validation should appear

  @directors-validations-2
  Scenario: Add a director page validation (incorrect information)
    When I wrongly fill in and submit the add a director page
    Then the add a director page incorrect value validation should appear

  @directors-validations
  Scenario: Director financial history page validation
    And I begin adding a new director and their details
    When I submit the empty page
    Then the director financial history page empty field validation should appear

  @directors-validations @director-conviction-declaration-required @vol763
  Scenario: A director conviction declaration requires an answer
    When I reach the new director conviction declaration
    And I submit the director conviction declaration without an answer
    Then the director conviction declaration shows "Value is required" in the summary and inline

  @director-conviction-required-fields @vol763
  Scenario: A previous conviction requires its details
    When I reach the new director conviction declaration
    And I submit an empty previous conviction for the director
    Then the director conviction required fields show errors in the summary and inline
      | data[title]        | Choose title                              |
      | data[forename]     | Enter first name                          |
      | data[familyName]   | Enter last name                           |
      | dob_day            | Enter date of conviction                  |
      | data[categoryText] | Enter a value for the field: "Offence"    |
      | data[notes]        | Please give details about the offence     |
      | data[courtFpn]     | Provide the name of the court you attended |
      | data[penalty]      | Tell us what penalty you received         |