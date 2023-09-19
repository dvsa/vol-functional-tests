@sub-app @ss_regression @printAndSign @localsmoke
Feature: Complete an application manually

  Scenario Outline: Submit an application
    Given i have a "<OperatorType>" application in progress
    And i submit and pay for the application
    Then the application should be under consideration

    Examples:
      | OperatorType |
      | Goods        |

