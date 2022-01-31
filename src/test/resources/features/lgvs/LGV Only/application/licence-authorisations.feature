Feature: Licence Authorisation page checks and validations

  Background:
    Given I have a "GB" lgv only application
    And i have logged in to self serve

  Scenario: Operating Centres are not present with no ability to add them and all information is for LGVs only
    When i navigate to the application licence authorisation page
    Then there is no add operating centre button
    And the only field and information is for LGVs only

  Scenario: LGV Authorisation value should be between 1-5000
    And i navigate to the application licence authorisation page
    When i try to save an authorisation exceeding the valid values
    Then a maximum authorisation value error message should appear

  Scenario: UK Community Authorisation value should be between 0-5000
    And i navigate to the application licence authorisation page
    When i try to save a community authorisation exceeding the valid values
    Then a maximum community authorisation value error message should appear

  Scenario: UK Community Authorisations don't exceed LGV Authorisations
    And i navigate to the application licence authorisation page
    When i enter an lgv authorisation and a higher community authorisation
    Then the community authorisation exceeding lgv authorisation error appears
