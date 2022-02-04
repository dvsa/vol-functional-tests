@lgv
Feature: LGV Only interims are applicable and only display LGV Only related information and submissions

  Background:
    Given I have a "GB" lgv only application
    And I submit the application with an interim

  Scenario: Can see only LGV Authority on Interim requested licence screen
    When i view the application interim on internal
    Then only the LGV related details are displayed on the interim

  Scenario: If number is not entered when trying to grant licence then error is triggered
    And i view the application interim on internal
    When i try to grant the interim without entering an authority
    Then an interim authority value is required error message should display

  Scenario: If caseworker exceeds the authority then error is triggered
    And i view the application interim on internal
    When i enter an interim lgv authority exceeding the amount on the application
    Then an interim lgv auth value exceeds application lgv authority value error message should display

  Scenario: If authority is 0 then error is triggered
    And i view the application interim on internal
    When i enter zero in the interim lgv authority
    Then a zero lgv auth interim error message should display