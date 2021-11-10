@prod_smokeTest
Feature: Submit an application on pre-prod or int env

  Scenario: Submit an application
    Given i have a self serve account
    And i start a new licence application
    When i submit and pay for the application
    Then the application should be submitted