@SS
@OLCS-21213
@SS-VERIFY

Feature: Operator signs with verify

  Scenario: Operator chooses to sign with verify

    Given i have an application in progress
    When i choose to sign with verify
    Then the application should be signed with verify