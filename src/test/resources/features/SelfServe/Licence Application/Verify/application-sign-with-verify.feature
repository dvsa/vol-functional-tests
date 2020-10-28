@SS
@OLCS-7284
@SS-Verify-On
@ss_regression
@gov-verify
@browser-test

Feature: Operator signs with verify

  Scenario Outline: Operator chooses to sign with verify
    And i have an application in progress
    When i choose to sign with verify with "<user>"
    Then the application should be signed with verify

    Examples:
      | user   |
      | pavlov |