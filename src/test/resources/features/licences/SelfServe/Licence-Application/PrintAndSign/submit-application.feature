@prod_smoketest

Feature: Submit an application on pre-prod or int env

  Scenario: Submit an application
    Given i start a new licence application
    When i submit the application