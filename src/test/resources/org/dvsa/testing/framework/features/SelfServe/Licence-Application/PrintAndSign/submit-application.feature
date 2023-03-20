@prod_smoke
@ss_regression
Feature: Complete an application manually

  Scenario Outline: Submit an application
    Given i have a self serve account
    And i submit and pay for a "<Licence>" licence application
    Then the licence should be granted by a caseworker
    And the application status should be "awaiting fee"


    Examples:
      | Licence |
      | Goods   |