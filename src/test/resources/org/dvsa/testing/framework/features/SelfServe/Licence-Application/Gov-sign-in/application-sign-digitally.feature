@OLCS-7284
@ss_regression
@gov-sign-in
@smoketest

Feature: Operator signs digitally

  Scenario Outline: Operator chooses to sign digitally
    And i have a "<OperatorType>" application in progress
    When I can navigate to gov sign in
    And I sign in to gov sign in to complete the process
    Then the application should be digitally signed

    Examples:
      | OperatorType |
      | Goods        |
#      | Public Public      |