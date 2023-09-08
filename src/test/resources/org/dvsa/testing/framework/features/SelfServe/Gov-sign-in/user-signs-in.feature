@gov-sign-in
Feature: Gov sign in

  Background:
    Given i have a "Goods" application in progress

  Scenario: Login to sign in
  When I can navigate to gov sign in
  Then I sign in to gov sign in to complete the process
  And I am taken back to VOL Review and Declarations page
  Then i complete the payment process
  Then the application should be submitted

  @gov-reg
Scenario: Register a GOV sign in account
  When I can navigate to gov sign in
  Then I register a gov sign in account to complete the process




