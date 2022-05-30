@OLCS-7284
@SS-Verify-On
@ss_regression
@gov-verify
@smoketest

Feature: Operator signs with verify

  Scenario: Operator chooses to sign with verify
    And i have an application in progress
    When i choose to sign with verify
    Then the application should be signed with verify