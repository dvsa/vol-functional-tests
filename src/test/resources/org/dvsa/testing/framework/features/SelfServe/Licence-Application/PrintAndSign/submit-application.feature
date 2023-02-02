@prod_smokeTest
@ss_regression
@Accessibility
Feature: Complete an application manually

  Scenario Outline: Submit an application
    Given i have a self serve account
    And i have no existing applications
    And i start a new "<Licence>" licence application
    When i submit and pay for the application
    Then the application should be submitted
    And redirected to the dashboard

    Examples:
      | Licence |
      | Public   |
      | Goods   |