@prod_smokeTest
@ss_regression
@Accessibility
Feature: Complete an application manually

  Scenario: Submit an application
    Given i have a self serve account
    And i have no existing accounts
    And i start a new licence application
    When i submit and pay for the application
    Then the application should be submitted