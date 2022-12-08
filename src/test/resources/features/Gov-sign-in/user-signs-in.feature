@gov-sign-in
Feature: Gov sign in

Scenario: Login to sign in
  And i have an application in progress
  When I can navigate to gov sign in
  Then I sign in to gov sign in to complete the process
  And I am taken back to VOL
  And I pay fee for application





