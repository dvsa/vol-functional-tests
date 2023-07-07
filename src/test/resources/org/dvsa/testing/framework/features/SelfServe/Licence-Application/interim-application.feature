Feature: Check interim fees

  Scenario: Apply for an interim licence
    Given i have an interim {string} {string} application
    Given i have a "goods" application in progress
    And i choose to apply for an interim licence
    Then i should be presented with an interim licence fee