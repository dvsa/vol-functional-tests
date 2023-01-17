@OLCS-7284
@SS-Verify-On
@ss_regression
@gov-sign-in
@smoketest

Feature: Operator signs digitally

  Scenario: Operator chooses to sign digitally
    And i have an application in progress
    When I can navigate to gov sign in
    And I sign in to gov sign in to complete the process
    Then the application should be digitally signed