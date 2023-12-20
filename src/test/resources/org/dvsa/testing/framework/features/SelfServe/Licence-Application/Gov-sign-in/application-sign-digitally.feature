@OLCS-7284 @ss_regression @gov-sign-in @smoketest @FullRegression
Feature: Operator signs digitally

  @reads-and-writes-system-properties
  Scenario Outline: Operator chooses to sign digitally
    Given i have a "<OperatorType>" application in progress
    When I can navigate to gov sign in
    And I sign in to gov sign in to complete the process
    Then the application should be digitally signed

    Examples:
      | OperatorType |
      | Goods        |
      | Public       |