@gov-sign-in
Feature: Gov sign in

  Background:
    Given i have an application in progress


  Scenario: Login to sign in
  When I can navigate to gov sign in
  Then I sign in to gov sign in to complete the process
  And I am taken back to VOL
  Then i complete the payment process
  Then the application should be submitted





